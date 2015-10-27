(ns koh.xml
  (:require [koh.err :refer [err?]]
            [cljs.core.async :as async]
            [clojure.string :refer [trim replace]]
            [clojure.walk :refer [postwalk]]))

(defn find-nodes [doc query]
  (let [res-type (.-ORDERED_NODE_SNAPSHOT_TYPE js/XPathResult)
        res (.evaluate js/document query doc nil res-type nil)
        len (.-snapshotLength res)
        nodes (map #(.snapshotItem res %) (range len))]
    (map #(.-textContent %) nodes)))

(defn browser-xpath [html? string queries]
  (let [doc (.parseFromString (js/DOMParser.) string (if html? "text/html" "text/xml"))]
    (async/to-chan [(zipmap (keys queries)
                            (map (partial find-nodes doc) (vals queries)))])))

(defn rnative-xpath [html? s queries]
  (let [out (async/chan)
        finder (if html? js/rnmxmlQueryHtml js/rnmxmlQueryXml)
        cb (fn[err results]
             (if (err? err)
               (async/onto-chan out [err])
               (async/onto-chan out [(zipmap (keys queries) (js->clj results))])))]
    (finder s (clj->js (vals queries)) cb)
    out))

(defn node-xpath [html? string queries]
  (let [require (.-require js/module)
        node-xpath (require "./xpath")
        node-xmldom (require "./xmldom")
        dom (.-DOMParser node-xmldom)
        doc (.parseFromString (dom.) string)
        find-nodes (fn[q] (map #(some-> (or (.-firstChild %) %) .-nodeValue)
                               (.select node-xpath q doc)))]
    (async/to-chan [(zipmap (keys queries)
                            (map find-nodes (vals queries)))])))

(defn rnative-parse [string html?]
  (let [out (async/chan)
        parser js/rnmxmlParseString
        keywordize-tags (fn[data](postwalk #(if (:tag %)
                                              (assoc % :tag (keyword (:tag %)))
                                              %) data))
        cb (fn[err results]
             (if (err? err)
               (async/onto-chan out [err])
               (async/onto-chan out [(keywordize-tags (js->clj results :keywordize-keys true))])))]
    (parser string html? cb)
    out))

(defn read-node [filter-node-type attr-name-prop attr-val-prop node]
  (let [get-attr (fn[node]
                   (let [attr (.-attributes node)]
                     (reduce merge (map #(let [at (aget attr %)]
                                           {(keyword (replace (aget at attr-name-prop) ":" "/")) (aget at attr-val-prop)})
                                        (range (.-length attr))))))
        get-childs (fn[node-list]
                     (let [childs (.call (aget js/Array "prototype" "slice") (.-childNodes node))]
                       (map (partial read-node filter-node-type attr-name-prop attr-val-prop)
                            (filter #(not= filter-node-type (.-nodeType %)) childs))))]
    {:tag (keyword (.-tagName node))
     :attrs (or (get-attr node) {})
     :content (if (and (= 1 (aget node "childNodes" "length"))
                       (= filter-node-type (.-nodeType (aget (.-childNodes node) 0))))
                [(trim (.-textContent node))]
                (get-childs node))}))

(defn browser-parse [string html?]
  (let [doc (.parseFromString (js/DOMParser.) string (if html? "text/html" "text/xml"))
        root (.-documentElement doc)]
    (async/to-chan [(read-node (.-TEXT_NODE js/Node)
                               "nodeName"
                               "nodeValue"
                               root)])))

(defn node-parse [string html?]
  (let [require (.-require js/module)
        node-xmldom (require "./xmldom")
        dom (.-DOMParser node-xmldom)
        doc (.parseFromString (dom.) string)
        root (.-documentElement doc)
        text-node-code 3]
    (async/to-chan [(read-node text-node-code
                               "name"
                               "value"
                               root)])))

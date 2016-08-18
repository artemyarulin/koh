(ns koh.xml.parser-client
  (:require [clojure.string :refer [trim replace blank?]]
            [clojure.walk :refer [postwalk]]
            [koh.environment :refer [platform]]))

;; TODO: Now when we have all the tests - refactor this piece of shit
(defn read-node [filter-node-type attr-name-prop attr-val-prop node]
  (let [get-attr (fn[node]
                   (if-let [attr (.-attributes node)]
                     (reduce merge (map #(let [at (aget attr %)]
                                           {(keyword (replace (aget at attr-name-prop) ":" "/")) (aget at attr-val-prop)})
                                        (range (.-length attr))))
                     {}))
        get-childs (fn[node-list]
                     (let [child-nodes (.-childNodes node)]
                       (if-let [childs (.call (aget js/Array "prototype" "slice") (or child-nodes []))]
                         (filter identity (map (partial read-node filter-node-type attr-name-prop attr-val-prop)
                                               (filter #(not= filter-node-type (.-nodeType %)) childs)))
                         [])))]
    (when-let [tag-name (some-> node .-tagName)]
      {:tag (keyword tag-name)
       :attrs (or (get-attr node) {})
       :content (if (and (= 1 (some-> node .-childNodes .-length))
                         (= filter-node-type (.-nodeType (aget (.-childNodes node) 0))))
                  (let [text-content (some-> (.-textContent node) trim)]
                    (if (blank? text-content)
                      []
                      [text-content]))
                  (get-childs node))})))

(defn rnative-parse [string html? cb]
  (let [parser js/rnmxmlParseString
        keywordize-tags (fn[data](postwalk #(if (:tag %)
                                              (assoc % :tag (keyword (:tag %)))
                                              %) data))
        handler (fn[err results]
                  (if (instance? js/Error err)
                    (cb err nil)
                    (cb nil (keywordize-tags (js->clj results :keywordize-keys true)))))]
    (parser string html? handler)))

(defn browser-parse [string html? cb]
  (let [doc (.parseFromString (js/DOMParser.) string (if html? "text/html" "text/xml"))
        root (.-documentElement doc)]
    (cb nil (read-node (.-TEXT_NODE js/Node)
                       "nodeName"
                       "nodeValue"
                       root))))

(defn node-parse [string html? cb]
  (let [require (.-require js/module)
        node-xmldom (require "./xmldom")
        dom (.-DOMParser node-xmldom)
        doc (.parseFromString (dom.) string)
        root (.-documentElement doc)
        text-node-code 3]
    (cb nil (read-node text-node-code
                       "name"
                       "value"
                       root))))

(def parser (case platform
              :browser browser-parse
              :rnative rnative-parse
              :node node-parse
              #(throw (js/Error. "Unsupported platform"))))

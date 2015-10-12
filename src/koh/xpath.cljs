(ns koh.xpath
  (:require [koh.err :refer [err?]]
            [cljs.core.async :as async]))

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
        cb (fn[res](if (err? res)
                     (async/onto-chan out [res])
                     (async/onto-chan out [(zipmap (keys queries) res)])))]
    (finder s (clj->js (vals queries)) cb)))

(defn node-xpath [html? string queries]
  (let [out (async/chan)
        require (.-require js/module)
        node-xpath (require "./xpath")
        node-xmldom (require "./xmldom")
        dom (.-DOMParser node-xmldom)
        doc (.parseFromString (dom.) string)
        find-nodes (fn[q] (map #(some-> (or (.-firstChild %) %) .-nodeValue)
                               (.select node-xpath q doc)))]
    (async/to-chan [(zipmap (keys queries)
                            (map find-nodes (vals queries)))])))

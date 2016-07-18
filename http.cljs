(ns koh.http
  (:require [koh.err :refer [err]]
            [cljs.core.async :as async]))

(defn node-http [method headers body url]
  "NodeJS transport. Using standard http.request API"
  (let [out (async/chan)
        require (.-require js/module)
        node-http (require "http")
        node-url (require "url")
        headers (if body (assoc headers "Content-Length" (count body)) headers)
        parsed-url (.parse node-url url)
        err-handler #(async/onto-chan out [%])
        cb (fn[resp](let [data (atom "")]
                      (.on resp "data" #(swap! data str %))
                      (.on resp "error" err-handler)
                      (.on resp "end" #(async/onto-chan out [{(.-statusCode resp)
                                                             @data}]))))
        req (.request node-http (js-obj "method" method
                                        "hostname" (.-hostname parsed-url)
                                        "path" (.-path parsed-url)
                                        "headers" (clj->js headers)) cb)]
    (when body (.write req body))
    (.on req "error" err-handler)
    (.end req)
    out))

(defn browser-http [method headers body url]
  "Browser transport, for debugging purpose only. Consider to run your
  browser with --disable-web-security in order to allow cross sites
  requests"
  (let [req (js/XMLHttpRequest.)
        out (async/chan)
        result-handler #(when (= (.-readyState req) 4)
                          (let [resp-code (.-status req)
                                resp-text (.-responseText req)
                                data (if (zero? resp-code)
                                       (err (str "Request failed: " resp-code resp-text))
                                       {resp-code resp-text})]
                            (async/onto-chan out [data])))]
    (aset req "onreadystatechange" result-handler)
    (.open req method url true)
    (doseq [[k v] headers] (.setRequestHeader req (name k) v))
    (.send req body)
    out))

(defn rnative-http [method headers body url]
  "React native HTTP transport. For now we rely on standard fetch
  function, later on we will move to react-native-raw-http"
  (let [out (async/chan)
        props-base {:method method
                    :headers (clj->js headers)}
        props (if body
                (assoc props-base :body body)
                props-base)
        req (js/fetch url (clj->js props))]
    (.catch req #(async/onto-chan out [(err (.-message %))]))
    (.then req (fn[resp](.then (.text resp) (fn[data](async/onto-chan out [{(.-status resp) data}])))))
    out))

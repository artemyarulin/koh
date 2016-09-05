(ns koh.http.transport-client
  (:require [koh.environment :refer [platform]]))

(defn ensure-body-string [body]
  (if (or (nil? body) (string? body))
    body
    (->> body clj->js (.stringify js/JSON))))

(defn node-http [method headers body url cb]
  "NodeJS transport. Using standard http.request API"
  (let [require (.-require js/module)
        node-http (require "http")
        node-url (require "url")
        headers (if body (assoc headers "Content-Length" (count (ensure-body-string body))) headers)
        parsed-url (.parse node-url url)
        err-handler #(cb %1 nil)
        handler (fn[resp](let [data (atom "")]
                           (.on resp "data" #(swap! data str %))
                           (.on resp "error" err-handler)
                           (.on resp "end" #(cb nil {:status (.-statusCode resp)
                                                     :body @data
                                                     :headers (js->clj (.-headers resp))}))))
        req (.request node-http (js-obj "method" method
                                        "hostname" (.-hostname parsed-url)
                                        "path" (.-path parsed-url)
                                        "headers" (clj->js headers)) handler)]
    (when body (.write req (ensure-body-string body)))
    (.on req "error" err-handler)
    (.end req)))

(defn browser-http [method headers body url cb]
  "Browser transport, for debugging purpose only. Consider to run your
  browser with --disable-web-security in order to allow cross sites
  requests"
  (let [req (js/XMLHttpRequest.)
        result-handler #(when (= (.-readyState req) 4)
                          (let [resp-code (.-status req)
                                resp-text (.-responseText req)]
                            (if (zero? resp-code)
                              (cb (ex-info (str "Request failed: " resp-code resp-text) {}) nil)
                              (cb nil {:status resp-code
                                       :body resp-text
                                       :headers {}}))))] ;;TODO: getAllResponseHeaders returns string and we have to parse it somehow
    (aset req "onreadystatechange" result-handler)
    (.open req method url true)
    (doseq [[k v] headers] (.setRequestHeader req (name k) v))
    (.send req (ensure-body-string body))))

(defn rnative-http [method headers body url cb]
  "React native HTTP transport. For now we rely on standard fetch
  function, later on we will move to react-native-raw-http"
  (let [props-base {:method method :headers (clj->js headers)}
        props (if body
                (assoc props-base :body (ensure-body-string body))
                props-base)
        req (js/fetch url (clj->js props))]
    (.catch req #(cb (ex-info (.-message %) {})))
    (.then req (fn[resp](.then (.text resp) (fn[data](cb nil {:status (.-status resp)
                                                              :body data
                                                              :headers {}}))))))) ;;TODO: response.headers.entires() should be used here

(def transport (case platform
                 :browser browser-http
                 :rnative rnative-http
                 :node node-http
                 #(throw (js/Error. "Unsupported platform"))))

;;;; Following namespace contains platform dependend code, all the IO
;;;; API has to be a part of this namespace as well as any platform
;;;; specific functions"

(ns koh.core
  (:require [cljs.core.async :as async]))

(defn err? [obj]
  "Platform dependend check if object is an error"
  (instance? js/Error obj))

(defn err [msg]
  "Platform dependend way to create an error object"
  (js/Error. msg))

(defn parse-json [s]
  "Returns either parsed json or returns error"
  (try (.parse js/JSON s)
       (catch js/Error er er)))

(defn to-json [obj]
  "Convertt object to JSON string"
  (.stringify js/JSON obj))

(def cur-platform (cond (and (exists? js/window) (exists? js/document)) :browser
                        (and (exists? js/module) (exists? (.-exports js/module))) :node
                        :else (throw (err "Not supported platform"))))

(defn enable-print! []
  "Enables printing to console"
  (set! *print-newline* false)
  (set! *print-fn*
    (fn [& args]
      (.apply (.-log js/console) js/console (into-array args))))
  (set! *print-err-fn*
    (fn [& args]
      (.apply (.-error js/console) js/console (into-array args))))
  nil)

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

(def http
  "Returns network transport for current platform. Transport sends
  HTTP request with specified parameters. Returns either error or map
  {http-response-code response}"
  (case cur-platform
    :browser browser-http
    :node node-http
    (throw (err "Unsupported platform"))))

(set! *main-cli-fn* (constantly nil))

(ns koh.core
  (:require [cljs.core.async :as async]
            [koh.err :as err]
            [koh.json :as json]
            [koh.http :refer [node-http browser-http rnative-http]]
            [koh.xpath :refer [browser-xpath rnative-xpath node-xpath]]))

(def parse-json json/parse-json)
(def to-json json/to-json)
(def err err/err)
(def err? err/err?)

(def cur-platform (cond (and (exists? js/window) (exists? js/document)) :browser
                        (and (exists? js/module) (exists? (.-exports js/module))) :node
                        (and (exists? js/React) (exists? js/process) (exists? js/fetch)) :rnative
                        :else (throw (err "Not supported platform"))))

(def http
  "HTTP transport for the current platform. Accepts method, headers, body and url.
   Returns channel with either error or map {http-response-code
   response}"
  (case cur-platform
    :browser browser-http
    :node node-http
    :rnative rnative-http
    (throw (err "Unsupported platform"))))

(def xpath
  "XPath query for the current platform. Accepts html? string queries
  where queries is a dictionary with xpath queries as values. Returns
  channel with eiter error or dictionary with values replaced with
  found value"
  (case cur-platform
    :browser browser-xpath
    :rnative rnative-xpath
    :node node-xpath
    (throw (err "Unsupported platform"))))

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

(set! *main-cli-fn* (constantly nil))

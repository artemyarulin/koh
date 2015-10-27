(ns koh.core
  (:require [cljs.core.async :as async]
            [koh.err :as err]
            [koh.json :as json]
            [koh.http :refer [node-http browser-http rnative-http]]
            [koh.xml :refer [browser-xpath rnative-xpath node-xpath browser-parse rnative-parse node-parse]]))

(def parse-json json/parse-json)
(def to-json json/to-json)
(def err err/err)
(def err? err/err?)

(def cur-platform (cond (and (exists? js/window) (exists? js/document)) :browser
                        (and (exists? js/GLOBAL) (exists? js/Text) (exists? js/Image)) :rnative
                        (and (exists? js/module) (exists? (.-exports js/module))) :node))

(defmulti http
  "HTTP transport for the current platform. Accepts method, headers, body and url.
   Returns channel with either error or map {http-response-code response}"
  (constantly cur-platform))

(defmethod http :browser [& args] (apply browser-http args))
(defmethod http :node [& args] (apply node-http args))
(defmethod http :rnative [& args] (apply rnative-http args))
(defmethod http :default [& args] (throw (err (str "Unsupported platform: " cur-platform))))

(defmulti xpath
  "XPath query for the current platform. Accepts html? string queries
  where queries is a dictionary with xpath queries as values. Returns
  channel with eiter error or dictionary with values replaced with
  found value"
  (constantly cur-platform))

(defmethod xpath :browser [& args] (apply browser-xpath args))
(defmethod xpath :rnative [& args] (apply rnative-xpath args))
(defmethod xpath :node [& args] (apply node-xpath args))
(defmethod xpath :default [& args] (throw (err "Unsupported platform")))

(defmulti parse-xml
  "Parses xml/html and returns similar data structures as clojure data.xml
  Accepts string and html? flag"
  (constantly cur-platform))

(defmethod parse-xml :browser [& args] (apply browser-parse args))
(defmethod parse-xml :rnative [& args] (apply rnative-parse args))
(defmethod parse-xml :node [& args] (apply node-parse args))
(defmethod parse-xml :default [& args] (throw (err "Unsupported platform")))

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

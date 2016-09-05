(ns koh.string
  (:require [clojure.string :as string]
            #?(:cljs [goog.crypt.base64 :as base64]
               :clj [clojure.data.codec.base64 :as base64])))

(defn displace [s & args]
  "Replaces token like {0}, {1} in a string using supplied parameters"
  (if (not args)
    s
    (recur (string/replace s
                           (re-pattern (str "\\{" (-> args count dec) "\\}"))
                           (-> args last str))
           (-> args vec butlast))))

(defn str->int [def-value s]
  "Converts string to int. If it's not possible - def-value returned instead"
  (if-let [n (and s (re-find #"^\d+$" s))]
    #?(:clj (Integer. n)
       :cljs (js/parseInt n 10))
    def-value))

(defn str->base64 [s]
  "Returns Base64 encoded representation of string"
  #?(:cljs (base64/encodeString s)
     :clj (String. (base64/encode (.getBytes s)) "UTF-8")))

(defn base64->str [s]
  "Decode string from Base64 encoded string. "
  #?(:cljs (base64/decodeString s)
     :clj (try (String. (base64/decode (.getBytes s)) "UTF-8")
               (catch Exception e ""))))

(ns koh.string
  (:require [clojure.string :as string]))

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

(defn date-time []
  "Returns current date time as a string in ISO format"
  #?(:cljs (.toISOString (js/Date.))
     :clj (str (java.time.LocalDateTime/now))))

(ns koh.string
  (:require [clojure.string :refer [replace]]))

(defn displace [s & args]
  "Replaces token like {0}, {1} in a string using supplied parameters"
  (if (not args)
    s
    (recur (replace s
                    (re-pattern (str "\\{" (-> args count dec) "\\}"))
                    (-> args last str))
           (-> args vec butlast))))

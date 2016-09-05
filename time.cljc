(ns koh.time)

(defn iso []
  "Returns current date time as a string in ISO format"
  #?(:cljs (.toISOString (js/Date.))
     :clj (str (java.time.LocalDateTime/now))))

(defn unix []
  "Returns current date time as a unix time in milleseconds since 1.1.1970 00:00:00"
  #?(:cljs (.now js/Date)
     :clj (System/currentTimeMillis)))

(defn run-after [ms f]
  "Run function after delay. On JVM blocks the thread. Can be used for
   waiting of external side effects to happen"
  #?(:cljs (js/setTimeout f ms)
     :clj (do (deref (promise) ms nil)
              (f))))

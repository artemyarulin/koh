(ns koh.err)

(defn err?
  "Returns true if value is an instance of ExceptionInfo or js/Error"
  [v]
  #?(:cljs (or (instance? ExceptionInfo v) (= (type v) js/Error))
     :clj (instance? clojure.lang.ExceptionInfo v)))

(defn err-msg
  "Returns error message"
  [v]
  #?(:cljs (ex-message v)
     :clj (.getMessage v)))

#?(:cljs
(defn err->js [err]
  "Converts error value to js/Error object"
  (if (= (type err) js/Error) err (-> err ex-message js/Error.))))

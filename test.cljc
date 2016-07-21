(ns koh.test
  #?(:cljs (:require [cljs.test])))

(defn async [f cb]
  "Async test helpers which uniform way of working with async functions from CLJC envionment"
  #?(:clj (let [out (promise)
                handler (fn[& args]
                          (apply cb args)
                          (deliver out nil))]
            (f handler)
            @out)
     :cljs (cljs.test/async done
                            (let [handler (fn[& args]
                                            (apply cb args)
                                            (done))]
                              (f handler)))))

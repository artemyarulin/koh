(ns koh.test
  #?(:cljs (:require [cljs.test])))

(defn async [f cb]
  "Async test helper which uniform way of working with async functions
  from CLJC envionment. Accept function and callback which would be
  applied to that function"
  #?(:clj (let [out (promise) handler (fn[& args]
                                        (apply cb args)
                                        (deliver out args))]
            (f handler)
            @out)
     :cljs (cljs.test/async done
                            (let [handler (fn[& args]
                                            (apply cb args)
                                            (done))]
                              (f handler)))))

(defn async-all [xs]
  "Async test helper which uniform way of working with async functions
  from CLJC envionment. Accept a list of pairs of functions and callbacks"
  #?(:clj (mapv (partial apply async) xs)
     :cljs (cljs.test/async done
                            (letfn [(handler [ops]
                                      (if-let [[f cb] (first ops)]
                                        (f (fn[& args]
                                             (apply cb args)
                                             (handler (rest ops))))
                                        (done)))]
                              (handler xs)))))

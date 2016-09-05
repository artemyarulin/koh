(ns koh.test (:require [clojure.test]))

(defn async [f & cbs]
  "Async test helper which uniform way of working with async functions
  from CLJC envionment. Accept function and callbacks which would be
  applied to that function"
  #?(:clj (let [out (promise)
                handlers (->> cbs (map #(fn[& args]
                                          (apply % args)
                                          (deliver out args))))]
            (apply f handlers)
            @out)

     :cljs (cljs.test/async done
                            (let [handlers (->> cbs (map #(fn[& args]
                                                            (apply % args)
                                                            (done))))]
                              (apply f handlers)))))

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

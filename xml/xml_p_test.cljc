(ns koh.xml-p-test
  (:require #?(:clj [clojure.test :refer [is are deftest]]
               :cljs [cljs.test :refer-macros [is are deftest async]])
            [koh.xml-p :refer [parse]]
            [promesa.core :as p]))

(deftest xml-p-test
  (let [in "<doc a='1'/>"
        exp {:tag :doc :attrs {:a "1"} :content []}]
    #?(:clj (is (.equals @(parse in false) exp))
       :cljs (async done (->> (parse in false)
                              (p/map #(is (= exp %)))
                              (p/map done))))))

#?(:clj
(deftest xml-p-err-case
  @(p/branch (parse "" false)
            (fn[_](is false))
            (fn[_](is true)))))

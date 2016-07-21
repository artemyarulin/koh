(ns koh.test-test
  (:require #?(:clj [clojure.test :refer [deftest are is]]
               :cljs [cljs.test :refer-macros [is are deftest]])
            [koh.test :refer [async]]))

(defn f-cb [v cb]
   (cb v))

(deftest cb-test
   (async (partial f-cb 42) #(is (= 42 %))))

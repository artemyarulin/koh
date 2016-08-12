(ns koh.test-test
  (:require [clojure.test :refer [deftest are is]]
            [koh.test :refer [async async-all]]))

(defn f-cb [v cb] (cb v))

(deftest async-cb
  (async (partial f-cb 42) #(is (= 42 %))))

(deftest async-all-cb
  (async-all (for [n (range 3)]
               [(partial f-cb n) #(is (= n %))])))

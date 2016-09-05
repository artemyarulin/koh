(ns koh.test-test
  (:require [clojure.test :refer [deftest are is]]
            [koh.test :refer [async async-all]]))

(defn f-cb [v cb] (cb v))
(defn f-no-cb [cb] (cb))

(deftest async+
  (async (partial f-cb 42) #(is (= 42 %))))

(deftest async-all+
  (async-all (for [n (range 3)]
               [(partial f-cb n) #(is (= n %))])))

(deftest async-multiple-callbacks-1
  (async (fn[cb1 cb2] (cb1 42))
         #(is (= % 42))
         #(is (= % 42))))

(deftest async-multiple-callbacks-2
  (async (fn[cb1 cb2] (cb2 42))
         #(is (= % 42))
         #(is (= % 42))))

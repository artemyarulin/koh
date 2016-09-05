(ns koh.time-test
  (:require [clojure.test :refer [deftest is]]
            [koh.time :as time]
            [koh.test :refer [async]]))

(deftest iso+
  (is (string? (time/iso))))

(deftest unix+
  (is (integer? (time/unix)))
  (let [t1 (time/unix)]
    (async (partial time/run-after 10)
           (fn[]
             (is (< t1 (time/unix)))))))

(deftest run-after+
  (let [t1 (time/unix)]
    (async (partial time/run-after 10)
           (fn[]
             (is (< t1 (time/unix)))))))

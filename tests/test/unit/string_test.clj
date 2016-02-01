(ns koh.string-test
  (:require [clojure.test :refer [is deftest]]
            [koh.string :refer [displace]]))

(deftest displace-should-work
  (is (= (displace "Hello {0}" "world") "Hello world"))
  (is (= (displace "{0}={1}" 2 2) "2=2"))
  (is (= (displace "{0}") "{0}"))
  (is (= (displace "{9}" 1) "{9}")))

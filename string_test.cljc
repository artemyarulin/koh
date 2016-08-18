(ns koh.string-test
  (:require [clojure.test :refer [is are deftest]]
            [koh.string :refer [displace str->int date-time str->base64 base64->str]]))

(deftest displace-t
  (is (= (displace "Hello {0}" "world") "Hello world"))
  (is (= (displace "{0}={1}" 2 2) "2=2"))
  (is (= (displace "{0}") "{0}"))
  (is (= (displace "{9}" 1) "{9}")))

(deftest str->int-t
  (are [d s r] (= (str->int d s) r)
    0 "22" 22
    0 "22x" 0
    -1 "a" -1
    42 "" 42
    42 nil 42
    -1 "011" 11))

(deftest date-time-test
  (is (date-time))
  (is (string? (date-time))))

(deftest base64-test
  (is (= "Hello" (-> "Hello" str->base64 base64->str)))
  (is (= "~!@#$%^&*()_+" (-> "~!@#$%^&*()_+" str->base64 base64->str)))
  (is (= "SGVsbG8=" (str->base64 "Hello")))
  (is (= "Hello" (base64->str "SGVsbG8=")))
  (is (= "" (-> "" str->base64 base64->str))))

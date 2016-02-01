(ns test.unit.core
  (:require [koh.core :refer [err err? enable-print!]]
            [cljs.test :refer-macros [deftest is testing]]))

(deftest err-test
  (is (err? (err "Error"))))

(deftest err-null-test
  (is (complement (err? nil))))

(deftest print-should-work
  (enable-print!)
  (print "OK"))

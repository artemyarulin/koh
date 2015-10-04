(ns test.unit.core
  (:require [koh.core :refer [err err?]]
            [cljs.test :refer-macros [deftest is testing]]))

(deftest err-test
  (is (err? (err "Error"))))

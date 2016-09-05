(ns koh.err-test
  (:require [clojure.test :refer [deftest is]]
            [koh.err :as err]))

(deftest err?-test
  (is (err/err? (ex-info "Error" {}))))

#?(:cljs
(deftest err->js-test
  (is (= "Error" (-> "Error" js/Error. err/err->js .-message)))))

(ns koh.environment-test
  (:require [clojure.test :refer [is are deftest]]
            [koh.environment :refer [platform]]))

(deftest platform-test
  (is (= platform #?(:clj :jvm :cljs :browser))))

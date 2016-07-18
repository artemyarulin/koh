(ns koh.environment-test
  (:require #?(:clj [clojure.test :refer [is are deftest]]
               :cljs [cljs.test :refer-macros [is are deftest]])
            [koh.environment :refer [platform]]))

(deftest platform-test
  (is (= platform #?(:clj :jvm :cljs :browser))))

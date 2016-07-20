(ns koh.http-p-test
  (:require #?(:clj [clojure.test :refer [is are deftest]]
               :cljs [cljs.test :refer-macros [is are deftest async]])
            [koh.http-p :refer [request]]
            [promesa.core :as p]))

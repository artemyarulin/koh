(ns ^:figwheel-always test.runner
  (:require [cljs.test :as test]
            [doo.runner :refer-macros [doo-tests]]
            [koh.core :refer [cur-platform enable-print!]]
            ;; Unit
            [test.unit.core]
            [test.unit.xml]
            ;; Integration
            [test.integration.http]
            [test.integration.xml]))

(enable-print!)

(when (= :node cur-platform)
  (.on js/process
       "uncaughtException"
       (fn[err]
         (.log js/console "Unhandled error:" err.stack))))

(goog-define suite "none")

(defn ^:export test-unit []
  (doo-tests 'test.unit.core
             'test.unit.xml))

(defn ^:exrpot test-integration []
  (doo-tests 'test.integration.http
             'test.integration.xml))

(case suite
  "unit" (test-unit)
  "integration" (test-integration)
  nil)

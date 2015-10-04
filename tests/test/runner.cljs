(ns ^:figwheel-always test.runner
  (:require [cljs.test :as test]
            [doo.runner :refer-macros [doo-tests]]
            [koh.core :refer [cur-platform]]
            ;; Unit
            [test.unit.core]
            ;; Integration
            [test.integration.core]))

(when (= :node cur-platform)
  (.on js/process
       "uncaughtException"
       (fn[err]
         (.log js/console "Unhandled error:" err.stack))))

(goog-define suite "none")

(case suite
  "unit" (doo-tests 'test.unit.core)
  "integration" (doo-tests 'test.integration.core)
  nil)

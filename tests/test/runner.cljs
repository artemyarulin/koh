(ns ^:figwheel-always test.runner
  (:require [cljs.test :as test]
            [doo.runner :refer-macros [doo-tests]]
            [koh.core :refer [cur-platform enable-print!]]
            ;; Unit
            [test.unit.core]
            [test.unit.xpath]
            ;; Integration
            [test.integration.http]))

(enable-print!)

(when (= :node cur-platform)
  (.on js/process
       "uncaughtException"
       (fn[err]
         (.log js/console "Unhandled error:" err.stack))))

(goog-define suite "none")

(case suite
  "unit" (doo-tests 'test.unit.core
                    'test.unit.xpath)
  "integration" (doo-tests 'test.integration.http)
  nil)

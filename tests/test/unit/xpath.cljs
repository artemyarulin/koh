(ns test.unit.xpath
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [koh.core :refer [xpath]]
            [cljs.core.async :refer [<!] :as async]
            [cljs.test :refer-macros [deftest is async testing]]))

(def node-values (partial xpath false))

(deftest xpath-node-values (async done (go
  (is (= (<! (node-values "<doc a='V'>V2</doc>" {:a "/doc/@a" :b "/doc"}))
         {:a ["V"] :b ["V2"]}))
  (is (= (<! (node-values "<doc a='V'>V2</doc>" {:a "/nope" :b "/doc"}))
         {:a [] :b ["V2"]}))
  (done))))

(deftest xpath-node-multiple (async done (go
  (is (= (<! (node-values "<doc><a>1</a><a>2</a></doc>" {:a "/doc/a"}))
         {:a ["1" "2"]}))
  (is (= (<! (node-values "<doc><A>1</A><A>2</A><B><C>3</C><C>4</C></B></doc>"
                          {:a "/doc/A" :b "/doc/B/C"}))
         {:a ["1" "2"] :b ["3" "4"]}))
  (done))))

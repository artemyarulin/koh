(ns test.integration.http
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [koh.core :refer [http parse-json err?]]
            [cljs.core.async :refer [<!]]
            [cljs.test :refer-macros [deftest is async testing]]))

(deftest http-test (async done (go
  (let [http-get (partial http "GET" nil nil)
        resp (<! (http-get "http://example.com"))]
    (is (not (err? resp)) "There should be no error")
    (is (contains? resp 200) "There should be right status code")
    (is (re-find #"example" (resp 200)) "There should be right content")
    (done)))))

(deftest network-error (async done (go
  (let [http-get (partial http "GET" nil nil)
        resp (<! (http-get "http://-example.com"))]
    (is (err? resp) "There should error")
    (done)))))

(deftest json-test
  (let [json-s "{\"id\":123}"
        obj (parse-json json-s)]
    (is (= 123 (aget obj "id")))))

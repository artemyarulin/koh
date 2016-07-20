(ns koh.http
  (:require #?(:cljs [koh.http.transport-client :refer [transport]]
               :clj [koh.http.transport-server :refer [transport]])))

(def request
  "Makes HTTP request. Accept method, headers, body and url with following callback. Callback will be called with err, value, where value is {[status-code] [response]}"
  transport)

(ns koh.http.transport-server
  (:require [clj-http.client :refer [request]]
            [clojure.data.json :refer [write-str]]))

(defn transport [method headers body url cb]
  (try
    (let [resp (request {:method method
                         :headers headers
                         :body (if (or (nil? body) (string? body))
                                 body
                                 (write-str body))
                         :url url
                         :throw-exceptions false})]
      (cb nil (select-keys resp [:status :body :headers])))
    (catch Exception e (cb e nil))))

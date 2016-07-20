(ns koh.http.transport-server
   (:require [clj-http.client :refer [request]]))

(defn transport [method headers body url cb]
  (try
    (let [resp (request {:method method
                         :headers headers
                         :body body
                         :url url
                         :throw-exceptions false})]
      (cb nil {(:status resp) (:body resp)}))
    (catch Exception e (cb e nil))))

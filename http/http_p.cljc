(ns koh.http-p
  (:require [koh.http :as http]
            [promesa.core :as p]))

(defn request [& args]
  "Returns promise on top of koh.http/request async call"
  (p/promise (fn [resolve reject]
     (let [cb (fn[err value]
                (if err
                  (reject value)
                  (resolve value)))]
       (apply http/request (conj (vec args) cb))))))

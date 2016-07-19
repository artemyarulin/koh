(ns koh.xml-p
  (:require [koh.xml :as xml]
            [promesa.core :as p]))

(defn parse [& args]
  "Returns promise on top of koh.xml/parse async call"
  (p/promise (fn [resolve reject]
     (let [cb (fn[err value]
                (if err
                  (reject value)
                  (resolve value)))]
       (apply xml/parse (conj (vec args) cb))))))

(ns koh.xml.server-parser
  (:require [clojure.zip :refer [xml-zip]]
            [clojure.data.xml :refer [parse]]))

(defn parser [string html? cb]
  (try
    (let [out (-> string java.io.StringReader. (#(parse % :namespace-aware false)) xml-zip)]
      (cb nil out))
    (catch Exception e (cb e nil))))

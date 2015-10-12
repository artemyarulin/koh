(ns koh.json)

(defn parse-json [s]
  "Returns either parsed json or returns error"
  (try (.parse js/JSON s)
       (catch js/Error er er)))

(defn to-json [obj]
  "Convertt object to JSON string"
  (.stringify js/JSON obj))

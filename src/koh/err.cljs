(ns koh.err)

(defn err? [obj]
  "Platform dependend check if object is an error"
  (instance? js/Error obj))

(defn err [msg]
  "Platform dependend way to create an error object"
  (js/Error. msg))

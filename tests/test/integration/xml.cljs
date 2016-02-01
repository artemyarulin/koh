(ns test.integration.xml
  (:require-macros [test.context :refer [xml-files]]
                   [cljs.core.async.macros :refer [go-loop go]])
  (:require [koh.core :refer [to-xml parse-xml]]
            [clojure.data :refer [diff]]
            [cljs.test :refer-macros [deftest is async testing]]))

(def files (xml-files))

(deftest parse-xml<>to-xml (async done (go
  (go-loop [files files]
    (if-let [file (first files)]
      (do (println "File:" (key file))
          (let [parsed-file (<! (parse-xml (val file) false))
                file-string (to-xml parsed-file)
                reparsed-file (<! (parse-xml file-string false))]
            (is (= parsed-file reparsed-file)
                (str "There should be no difference between parsed and parsed-to-string-reparsed"
                     "\n in-first:" (first (diff parsed-file reparsed-file))
                     "\n in-second:" (second (diff parsed-file reparsed-file))
                     "\n parsed-file:" parsed-file
                     "\n file-string:" file-string
                     "\n reparsed-file:" reparsed-file))
            (recur (rest files))))
      (done))))))

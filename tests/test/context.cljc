(ns test.context)

#?(:clj
   (defmacro xml-files []
     (let [files (rest (file-seq (clojure.java.io/file
                                  "./tests/test/xml")))]
       (zipmap (map #(.getName %) files)
               (map slurp files)))))

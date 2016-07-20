(ns koh.xml
  (:require #?(:cljs [koh.xml.parser-client :refer [parser]]
               :clj [koh.xml.parser-server :refer [parser]])))

(def parse
  "Parses supplied string into XML tree, calling cb value with err and xml. html? flag supported on non JVM environment and allows to parse mailformed XML, such as HTML "
  parser)

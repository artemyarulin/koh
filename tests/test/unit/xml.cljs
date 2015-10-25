(ns test.unit.xml
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [koh.core :refer [xpath parse-xml]]
            [cljs.core.async :refer [<!] :as async]
            [cljs.test :refer-macros [deftest is async testing]]))

(def node-values (partial xpath false))

(deftest xpath-node-values (async done (go
  (is (= (<! (node-values "<doc a='V'>V2</doc>" {:a "/doc/@a" :b "/doc"}))
         {:a ["V"] :b ["V2"]}))
  (is (= (<! (node-values "<doc a='V'>V2</doc>" {:a "/nope" :b "/doc"}))
         {:a [] :b ["V2"]}))
  (done))))

(deftest xpath-node-multiple (async done (go
  (is (= (<! (node-values "<doc><a>1</a><a>2</a></doc>" {:a "/doc/a"}))
         {:a ["1" "2"]}))
  (is (= (<! (node-values "<doc><A>1</A><A>2</A><B><C>3</C><C>4</C></B></doc>"
                          {:a "/doc/A" :b "/doc/B/C"}))
         {:a ["1" "2"] :b ["3" "4"]}))
  (done))))

(deftest parse-string-simple (async done (go
  (let [s "<doc a='V'>V2</doc>"
        expected {:tag "doc"
                  :attrs {:a "V"}
                  :content ["V2"]}]
    (is (= (<! (parse-xml s false)) expected)))
    (done))))

(deftest parse-string-complex (async done (go
  (let [s "<?xml version=\"1.0\" encoding=\"utf-8\" ?>
<SyncFolderItems xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"
                     xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">
  <ItemShape xmlns=\"http://schemas.microsoft.com/exchange/services/2006/messages\">
    <BaseShape xmlns=\"http://schemas.microsoft.com/exchange/services/2006/types\">Default</BaseShape>
  </ItemShape>
  <SyncFolderId xmlns=\"http://schemas.microsoft.com/exchange/services/2006/messages\">
    <DistinguishedFolderId Id=\"drafts\" xmlns=\"http://schemas.microsoft.com/exchange/services/2006/types\" />
  </SyncFolderId>
  <MaxChangesReturned xmlns=\"http://schemas.microsoft.com/exchange/services/2006/messages\">20</MaxChangesReturned>
</SyncFolderItems>"
        expected {:tag "SyncFolderItems"
                  :attrs {:xmlns/xsi "http://www.w3.org/2001/XMLSchema-instance"
                          :xmlns/xsd "http://www.w3.org/2001/XMLSchema"}
                  :content [{:tag "ItemShape"
                             :attrs {:xmlns "http://schemas.microsoft.com/exchange/services/2006/messages"}
                             :content [{:tag "BaseShape"
                                        :attrs {:xmlns "http://schemas.microsoft.com/exchange/services/2006/types"}
                                        :content ["Default"]}]}
                            {:tag "SyncFolderId"
                             :attrs {:xmlns "http://schemas.microsoft.com/exchange/services/2006/messages"}
                             :content [{:tag "DistinguishedFolderId"
                                        :attrs {:Id "drafts"
                                                :xmlns "http://schemas.microsoft.com/exchange/services/2006/types"}
                                        :content []}]}
                            {:tag "MaxChangesReturned"
                             :attrs {:xmlns "http://schemas.microsoft.com/exchange/services/2006/messages"}
                             :content ["20"]}]}]
    (is (= (<! (parse-xml s false)) expected)))
    (done))))

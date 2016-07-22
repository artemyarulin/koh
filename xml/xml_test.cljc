(ns koh.xml-test
  (:require #?(:clj [clojure.test :refer [is are deftest]]
               :cljs [cljs.test :refer-macros [is are deftest]])
            [koh.xml :refer [parse]]
            [koh.test :refer [async async-all]]))

(def tests {"<doc a='V'>V2</doc>" {:tag :doc
                                   :attrs {:a "V"}
                                   :content ["V2"]}
            "<doc/>" {:tag :doc
                      :attrs {}
                      :content []}
            "<?xml version=\"1.0\" encoding=\"utf-8\" ?>
<SyncFolderItems xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"
                     xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">
  <ItemShape xmlns=\"http://schemas.microsoft.com/exchange/services/2006/messages\">
    <BaseShape xmlns=\"http://schemas.microsoft.com/exchange/services/2006/types\">Default</BaseShape>
  </ItemShape>
  <SyncFolderId xmlns=\"http://schemas.microsoft.com/exchange/services/2006/messages\">
    <DistinguishedFolderId Id=\"drafts\" xmlns=\"http://schemas.microsoft.com/exchange/services/2006/types\" />
  </SyncFolderId>
  <MaxChangesReturned xmlns=\"http://schemas.microsoft.com/exchange/services/2006/messages\">20</MaxChangesReturned>
</SyncFolderItems>" {:tag :SyncFolderItems
                     :attrs {:xmlns/xsi "http://www.w3.org/2001/XMLSchema-instance"
                             :xmlns/xsd "http://www.w3.org/2001/XMLSchema"}
                     :content [{:tag :ItemShape
                                :attrs {:xmlns "http://schemas.microsoft.com/exchange/services/2006/messages"}
                                :content [{:tag :BaseShape
                                           :attrs {:xmlns "http://schemas.microsoft.com/exchange/services/2006/types"}
                                           :content ["Default"]}]}
                               {:tag :SyncFolderId
                                :attrs {:xmlns "http://schemas.microsoft.com/exchange/services/2006/messages"}
                                :content [{:tag :DistinguishedFolderId
                                           :attrs {:Id "drafts"
                                                   :xmlns "http://schemas.microsoft.com/exchange/services/2006/types"}
                                           :content []}]}
                               {:tag :MaxChangesReturned
                                :attrs {:xmlns "http://schemas.microsoft.com/exchange/services/2006/messages"}
                                :content ["20"]}]} })

(deftest parse-test
   (async-all (for [[input expected] tests]
     [(partial parse input false)
      (fn[err actual]
        (is (nil? err))
        (is (#?(:clj .equals :cljs =) expected actual)))])))

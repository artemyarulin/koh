(ns koh.xml-test
  (:require #?(:clj [clojure.test :refer [is are deftest]]
               :cljs [cljs.test :refer-macros [is are deftest async]])
            [koh.xml :refer [parse]]))

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

#?(:clj
  (deftest parse-test
    (for [[input expected] tests]
      (let [p (promise)
            _ (parse input false #(deliver p [%1 %2]))
            [err actual] @p]
        (is nil? err)
        (is (.equals actual expected))))))

#?(:cljs
  (deftest parse-test (async done
    (letfn [(handler [input expected cases err actual]
              (when input
                (is (nil? err))
                (is (= actual expected)))
              (if-let [[t-input t-expected] (first cases)]
                (parse t-input false (partial handler t-input t-expected (rest cases)))
                (done)))]
      (handler nil nil tests nil nil)))))

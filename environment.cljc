(ns koh.environment)

(def platform
  "Returns current execution platform: :browser :node :rnative :jvm or nil otherwise"
  #?(:cljs (cond (and (exists? js/window) (exists? js/document)) :browser
                 (and (exists? js/navigator) (= (.-product js/navigator) "ReactNative")) :rnative
                 (and (exists? js/module) (exists? (.-exports js/module))) :node)
     :clj :jvm))

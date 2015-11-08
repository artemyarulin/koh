(defproject koh "0.2.0"
  :description "Container of all platform depended code for ClojureSript with support of browser, node and react-native environment"
  :url "https://github.com/artemyarulin/koh"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.122"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [com.cemerick/piggieback "0.2.1"]
                 [lein-doo "0.1.5"]]
  :plugins [[lein-cljsbuild "1.1.0"]
            [lein-doo "0.1.5"]
            [lein-figwheel "0.4.0"]]
  :source-paths ["src" "tests"]
  :doo {:paths {:phantom "tests/phantom.sh"}}
  :figwheel {:nrepl-port 6800}
  :cljsbuild {:builds {:repl {:figwheel {:load-warninged-code true}
                              :source-paths ["src" "tests"]
                              :compiler {:language-in :ecmascript5
                                         :language-out :ecmascript5
                                         :output-to "target/repl.app.js"
                                         :output-dir "target"
                                         :asset-path "../target"
                                         :target :nodejs
                                         :main test.runner}}
                       :unit-browser {:source-paths ["src" "tests"]
                                      :compiler {:optimizations :simple
                                                 :language-in :ecmascript5
                                                 :language-out :ecmascript5
                                                 :main test.runner
                                                 :closure-defines {"test.runner.suite" "unit"}
                                                 :output-to "target/unit.app.js"}}
                       :unit-node {:source-paths ["src" "tests"]
                                   :compiler {:optimizations :simple
                                              :language-in :ecmascript5
                                              :language-out :ecmascript5
                                              :main test.runner
                                              :target :nodejs
                                              :closure-defines {"test.runner.suite" "unit"}
                                              :output-to "target/unit.app.js"}}
                       :integration-browser {:source-paths ["src" "tests"]
                                             :compiler {:optimizations :simple
                                                        :language-in :ecmascript5
                                                        :language-out :ecmascript5
                                                        :main test.runner
                                                        :closure-defines {"test.runner.suite" "integration"}
                                                        :output-to "target/integration.app.js"}}
                       :integration-node {:source-paths ["src" "tests"]
                                          :compiler {:optimizations :simple
                                                     :language-in :ecmascript5
                                                     :language-out :ecmascript5
                                                     :main test.runner
                                                     :target :nodejs
                                                     :closure-defines {"test.runner.suite" "integration"}
                                                     :output-to "target/integration.app.js"}}}})

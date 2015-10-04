(defproject koh "0.0.1"
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
                              :compiler {:output-to "target/repl.app.js"
                                         :output-dir "target"
                                         :asset-path "../target"
                                         :target :nodejs
                                         :main test.runner}}
                       :unit-browser {:source-paths ["src" "tests"]
                                      :compiler {:optimizations :simple
                                                 :main test.runner
                                                 :closure-defines {"test.runner.suite" "unit"}
                                                 :output-to "target/unit.app.js"}}
                       :unit-node {:source-paths ["src" "tests"]
                                   :compiler {:optimizations :simple
                                              :main test.runner
                                              :target :nodejs
                                              :closure-defines {"test.runner.suite" "unit"}
                                              :output-to "target/unit.app.js"}}
                       :integration-browser {:source-paths ["src" "tests"]
                                             :compiler {:optimizations :simple
                                                        :main test.runner
                                                        :closure-defines {"test.runner.suite" "integration"}
                                                        :output-to "target/integration.app.js"}}
                       :integration-node {:source-paths ["src" "tests"]
                                          :compiler {:optimizations :simple
                                                     :main test.runner
                                                     :target :nodejs
                                                     :closure-defines {"test.runner.suite" "integration"}
                                                     :output-to "target/integration.app.js"}}}})

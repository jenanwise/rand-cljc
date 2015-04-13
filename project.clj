(defproject rand-cljc "0.1.0"
  :description "Tiny portable explicit-PRNG randomizing functions."
  :url "http://github.com/jenanwise/rand-cljc"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0-beta1"]
                 [org.clojure/clojurescript "0.0-3196"]]

  :plugins [[lein-cljsbuild "1.0.5"]]

  :profiles {:dev {:dependencies [[org.clojure/core.async "0.1.346.0-17112a-alpha"]]}}

  :source-paths ["src"]
  :test-paths ["test/cljc"]
  :cljsbuild {:test-commands
              {"test" ["phantomjs"
                       "phantom/test.js"
                       "test.html"]}
              :builds [{:id "test"
                        :source-paths ["src" "test/cljc" "test/cljs"]
                        :compiler {:output-to "build/test/out.js"
                                   :output-dir "build/test/out"
                                   :cache-anlysis true
                                   :main rand-cljc.test-runner
                                   :optimizations :none}}]})

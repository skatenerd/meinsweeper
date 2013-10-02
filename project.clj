(defproject meinsweeper "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :profiles  {:dev  {:dependencies  [[speclj "2.5.0"]]}}
  :plugins [[speclj "2.5.0"]]
  :main meinsweeper.main-loop
  :test-paths ["spec/"]
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.0"]
                 [org.clojure/core.logic "0.8.3"]])

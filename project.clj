(defproject blorg "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [noir "1.3.0-beta7"]
                 [cssgen "0.3.0-alpha1"]
                 [cheshire "4.0.0"]]
  :profiles {:test
             {:dependencies
              [[clj-webdriver "0.6.0-alpha8"]]}}
  :main blorg.core)

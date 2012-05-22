(ns blorg.core-test
  (:use clojure.test
        clj-webdriver.taxi)
  (:require blorg.core))

(defn with-webapp [test]
  (set-driver! {:browser :firefox})
  (test)
  (quit))

(defn with-selenium [test]
  (blorg.core/start)
  (to "http://localhost:8080")
  (test)
  (blorg.core/stop))

(use-fixtures :each with-selenium)
(use-fixtures :once with-webapp)

(deftest front-page
  (testing "loads"
    (is (displayed? ".post-list"))))
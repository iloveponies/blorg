(ns blorg.core
  (:use noir.core)
  (:require [noir.server :as server]
            [hiccup.page :as page]))

(def *posts* [{:title "foo" :content "bar"}
              {:title "quux" :content "ref ref"}])

(defpage "/" []
  (page/html5
   (for [post *posts*]
     [:section
      [:h2 (:title post)]
      [:p (:content post)]])))

(defn -main [& args]
  (println "> blorg blog blorg")
  (server/start 8080))

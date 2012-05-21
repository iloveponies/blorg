(ns blorg.core
  (:use noir.core)
  (:use hiccup.form)
  (:require [noir.server :as server]
            [noir.response :as response]
            [hiccup.page :as page]))

(def posts (atom [{:title "foo" :content "bar"}
                  {:title "quux" :content "ref ref"}]))

(defn add-form []
  [:section
   [:h2 "Add post"]
   (form-to [:post "/"]
            (label "title" "Title")
            [:br]
            (text-field "title")
            [:br]
            (label "content" "Content")
            [:br]
            (text-area "content")
            [:br]
            (submit-button "Add"))])

(defn list-posts []
  [:section
   [:h2 "Posts"]
   (for [post @posts]
    [:section
     [:h3 (:title post)]
     [:p (:content post)]])])

(defpage "/" []
  (page/html5
   [:section
    (list-posts)
    (add-form)]))

(defpage [:post "/"] {:keys [title content]}
  (swap! posts #(conj % {:title title :content content}))
  (response/redirect "/"))

(defn -main [& args]
  (println "> blorg blog blorg")
  (server/start 8080))

(ns blorg.view
  (:use blorg.model
        noir.core)
  (:require [hiccup.form :as form]
            [hiccup.page :as page]
            [noir.response :as response]
            [cssgen :as css]))

(defn text-field [name placeholder]
  [:div {:class "text-field"}
   [:input {:type "textfield" :placeholder placeholder :name name}]])

(defn text-area [name placeholder]
  [:div {:class "text-area"}
   [:textarea {:placeholder placeholder :name name}]])

(defn add-form []
  [:section
   [:h2 "Add post"]
   (form/form-to [:post "/"]
                 (text-field "title" "Title")
                 (text-area "content" "Content")
                 [:button {:type "submit" :class "btn"} "Add"])])

(defn list-posts []
  [:section
   (for [post @posts]
    [:section
     [:h3 (:title post)]
     [:p (:content post)]])])

(defpage "/" []
  (page/html5
   [:head
    (page/include-css "css/base")
    (page/include-css "css/bootstrap")]
   [:body
    [:div {:class "container"}
     [:div {:class "row"}
      [:div {:class "span12"}
       [:header {:class "page-header"}
        [:h1 "Blorg"]]
       (list-posts)
       (add-form)]]]]))

(defpage [:post "/"] {:keys [title content]}
  (add-post {:title title :content content})
  (response/redirect "/"))

(defpage "/css/bootstrap" []
  (response/content-type
   "text/css"
   (slurp "bootstrap.css")))

(defpage "/css/base" []
  (response/content-type
   "text/css" (css/css [:html :font-family "sans-serif"])))
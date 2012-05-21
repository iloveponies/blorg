(ns blorg.core
  (:use noir.core)
  (:require [hiccup.form :as form])
  (:require [noir.server :as server]
            [noir.response :as response]
            [hiccup.page :as page]
            [cssgen :as css]))

(def posts (atom [{:title "foo" :content "bar"}
                  {:title "quux" :content "ref ref"}]))

(defn text-field [name placeholder]
  [:div {:class "text-field"}
   [:input {:type "textfield" :placeholder placeholder :name name}]])

(defn add-form []
  [:section
   [:h2 "Add post"]
   (form/form-to [:post "/"]
            [:div {:class "text-field"}
             (text-field "Title" "title")]
            [:div {:class "text-area"}
             (form/text-area {:placeholder "Content"} "content")]
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
  (swap! posts #(conj % {:title title :content content}))
  (response/redirect "/"))

(defpage "/css/bootstrap" []
  (response/content-type
   "text/css"
   (slurp "bootstrap.css")))

(defpage "/css/base" []
  (response/content-type
   "text/css" (css/css [:html :font-family "sans-serif"])
))

(defn -main [& args]
  (println "> blorg blog blorg")
  (server/start 8080))

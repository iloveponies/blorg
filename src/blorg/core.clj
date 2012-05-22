(ns blorg.core
  (:use noir.core)
  (:require [hiccup.form :as form])
  (:require [noir.server :as server]
            [noir.response :as response]
            [hiccup.page :as page]
            [cssgen :as css]
            [cheshire.core :as json])
  (:import [java.util Timer TimerTask]))

(def posts (atom nil))

(def blorg-server (atom nil))

(def timer (atom nil))

(defn save-posts []
  (spit "posts.json" (json/generate-string @posts)))

(defn load-posts-file [file]
  (json/parse-string (slurp file) true))

(defn load-posts []
  (swap! posts (fn [_] (load-posts-file "posts.json"))))

(defn start-save-timer []
  (swap! timer (fn [t] (Timer.)))
  (.schedule @timer (proxy [TimerTask] [] (run [] (save-posts))) 0 5000))

(defn stop-server []
  (swap! blorg-server
         (fn [s]
           (when (not (nil? s))
             (server/stop s))
           nil)))

(defn start-server []
  (swap! blorg-server
         (fn [_]
           (stop-server)
           (server/start 8080))))

(defn start []
  (load-posts)
  (start-save-timer)
  (start-server)
  "Started")

(defn stop []
  (server/stop @blorg-server)
  (.cancel @timer)
  (save-posts) ; Once more.
  "Stopped")

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
  (swap! posts #(conj % {:title title :content content}))
  (response/redirect "/"))

(defpage "/css/bootstrap" []
  (response/content-type
   "text/css"
   (slurp "bootstrap.css")))

(defpage "/css/base" []
  (response/content-type
   "text/css" (css/css [:html :font-family "sans-serif"])))

(defn -main [& args]
  (println "> blorg blog blorg")
  (server/start 8080))

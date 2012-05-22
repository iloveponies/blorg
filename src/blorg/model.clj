(ns blorg.model
  (:require [cheshire.core :as json])
  (:import [java.util Timer TimerTask]))

(def posts (atom nil))

(defn add-post [post]
  (swap! posts (fn [old-posts] (conj old-posts post))))

(defn save-posts []
  (spit "posts.json" (json/generate-string @posts)))

(defn load-posts-file [file]
  (json/parse-string (slurp file) true))

(defn load-posts []
  (reset! posts (load-posts-file "posts.json")))

(def timer (atom nil))

(defn start-save-timer []
  (reset! timer (Timer.))
  (.schedule @timer (proxy [TimerTask] [] (run [] (save-posts))) 0 5000))


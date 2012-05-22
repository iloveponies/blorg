(ns blorg.model
  (:require [cheshire.core :as json])
  (:import [java.util Timer TimerTask]))

(def posts (atom nil))

(defn save-posts []
  (spit "posts.json" (json/generate-string @posts)))

(defn load-posts-file [file]
  (json/parse-string (slurp file) true))

(defn load-posts []
  (swap! posts (fn [_] (load-posts-file "posts.json"))))

(def timer (atom nil))

(defn start-save-timer []
  (swap! timer (fn [t] (Timer.)))
  (.schedule @timer (proxy [TimerTask] [] (run [] (save-posts))) 0 5000))


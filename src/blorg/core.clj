(ns blorg.core
  (:use [blorg model view])
  (:use noir.core)
  (:require [noir.server :as server]))

(def blorg-server (atom nil))

(defn stop-server []
  (swap! blorg-server
         (fn [s]
           (when (not (nil? s))
             (server/stop s))
           nil)))

(defn start-server []
  (stop-server)
  (swap! blorg-server (fn [_] (server/start 8080))))

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

(defn -main [& args]
  (println "> blorg blog blorg")
  (start))

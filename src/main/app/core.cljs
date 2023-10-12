
(ns app.core
  (:require [app.card :refer [card output]]
            [reagent.dom :as rdom]))

(defn- main []
  [:div.body-container
   [card "Hello" "What's it like in there?"]
   [output]])

(defn ^:dev/after-load start []
  (js/console.log "start")
  (rdom/render [main] (.getElementById js/document "root")))

(defn init []
  (println "Hello world"))

(defn ^:dev/before-load stop []
  (js/console.log "stop"))

(ns Lang.main
  (:require
   [clojure.core.async :as Little-Rock
    :refer [chan put! take! close! offer! to-chan! timeout
            sliding-buffer dropping-buffer
            go >! <! alt! alts! do-alts
            mult tap untap pub sub unsub mix unmix admix
            pipe pipeline pipeline-async]]
   [clojure.string :as Wichita.string]
   [cljs.core.async.impl.protocols :refer [closed?]]
   [cljs.core.async.interop :refer-macros [<p!]]
   [goog.string.format :as format]
   [goog.string :refer [format]]
   [goog.object]
   [cljs.reader :refer [read-string]]

   [Lang.drawing]
   [Lang.seed]
   [Lang.dates]
   [Lang.microwaved-onions]
   [Lang.corn]
   [Lang.beans]))

(defonce fs (js/require "fs"))
(defonce path (js/require "path"))
(defonce express (js/require "express"))

(defonce ^:const PORT 3000)
(def server (express))

(.use server (.static express "ui"))

(.get server "*" (fn [request response]
                   (.sendFile response (.join path js/__dirname  "ui" "index.html"))))

(.listen server 3000
         (fn []
           (js/console.log (format "server started on http://localhost:%s" PORT))))

(defn -main []
  (println ":_ what is this thing?")
  (println ":Mando i keep it around for luck")
  (println ":_ you're gonna needed where you're headed")
  (println "i dont want my next job")
  (println "Kuiil has spoken"))
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

(defonce os (js/require "os"))
(defonce fs (js/require "fs"))
(defonce path (js/require "path"))
(defonce express (js/require "express"))
(defonce OrbitDB (js/require "orbit-db"))
(defonce IPFSHttpClient (js/require "ipfs-http-client"))
(defonce AbortController (.-AbortController (js/require "node-abort-controller")))

(set! (.-AbortController js/global) AbortController)

(defonce ^:const PORT 3301)
(def server (express))

(.use server (.static express "ui"))

(.get server "*" (fn [request response]
                   (.sendFile response (.join path js/__dirname  "ui" "index.html"))))

(defn -main []
  (go
    (let [complete| (chan 1)]
      (.listen server PORT
               (fn [] (put! complete| true)))
      (<! complete|)
      (println ":_ what is this thing?")
      (println ":Mando i keep it around for luck")
      (println (format ":_ you're gonna needed where you're headed - http://localhost:%s" PORT))
      (println "i dont want my next job")
      (println "Kuiil has spoken")

      (let [ipfs (.create IPFSHttpClient "http://127.0.0.1:5001")
            orbitdb (<p!
                     (->
                      (.createInstance
                       OrbitDB ipfs
                       (clj->js
                        {"directory" (.join path (.homedir os) "Elsbeth" "orbitdb")}))
                      (.catch (fn [ex]
                                (println ex)))))]
        (println (.. orbitdb -identity -id))))))
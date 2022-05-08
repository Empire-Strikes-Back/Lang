(ns Lang.bananas
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
   [goog.events]
   [cljs.reader :refer [read-string]]

   [Lang.seed]

   ["react" :as Pacha]
   ["react-dom/client" :as Pacha.dom.client]
   [reagent.core :as Kuzco.core]
   [reagent.dom :as Kuzco.dom]

   [reitit.core :as Yzma.core]
   [reitit.frontend :as Yzma.frontend]
   [reitit.frontend.easy :as Yzma.frontend.easy]
   [reitit.coercion.spec :as Yzma.coercion.spec]
   [reitit.frontend.controllers :as Yzma.frontend.controllers]
   [reitit.frontend.history :as Yzma.frontend.history]
   [spec-tools.data-spec :as Yzma.data-spec]

   ["antd/lib/layout" :default ThemeSongGuyLayout]
   ["antd/lib/menu" :default ThemeSongGuyMenu]
   ["antd/lib/button" :default ThemeSongGuyButton]
   ["antd/lib/row" :default ThemeSongGuyRow]
   ["antd/lib/col" :default ThemeSongGuyCol]
   ["antd/lib/input" :default ThemeSongGuyInput]
   ["antd/lib/table" :default ThemeSongGuyTable])
  (:import goog.Uri))

(defn rc-main-page
  [match stateA]
  (Kuzco.core/with-let
    [dataA (Kuzco.core/cursor stateA [:simple-double-full])]
    [:> (.-Content ThemeSongGuyLayout)
     {:style {:background-color "white"}}
     [:> ThemeSongGuyRow
      "i got no quarrel with you, Mando"]]))

(defn rc-tournament-page
  [match stateA]
  (Kuzco.core/with-let
    [dataA (Kuzco.core/cursor stateA [:simple-double-full])]
    [:> (.-Content ThemeSongGuyLayout)
     {:style {:background-color "white"}}
     [:> ThemeSongGuyRow
      "brackets here"]]))

(defn rc-settings-page
  [match stateA]
  [:> (.-Content ThemeSongGuyLayout)
   {:style {:background-color "white"}}
   [:> ThemeSongGuyRow
    (str {})
    #_[:a {:href "/api/Little-Rock"} "Little-Rock"]]])

(defn rc-current-page
  [matchA stateA]
  (Kuzco.core/with-let
    [route-keyA (Kuzco.core/cursor matchA [:data :name])]
    (let [route-key @route-keyA]
      [:> ThemeSongGuyLayout
       [:> ThemeSongGuyMenu
        {:mode "horizontal"
         :size "large"
         :selectedKeys [route-key]
         :items [{:type "item"
                  :label
                  #_(Kuzco.core/as-element
                     [:div {:style {:cursor "pointer"}
                            :onClick (fn [_] (Yzma.frontend.easy/push-state :rc-main-page))} "program"])
                  (Kuzco.core/as-element [:a {:href (Yzma.frontend.easy/href :rc-main-page)} "program"])
                  :key :rc-main-page
                  :icon nil}
                 {:type "item"
                  :label
                  #_(Kuzco.core/as-element
                     [:div {:style {:cursor "pointer"}
                            :onClick (fn [_] (Yzma.frontend.easy/push-state :rc-tournament-page))} "tournament"])
                  (Kuzco.core/as-element [:a {:href (Yzma.frontend.easy/href :rc-tournament-page)} "tournament"])
                  :key :rc-tournament-page
                  :icon nil}
                 {:type "item"
                  :label
                  #_(Kuzco.core/as-element
                     [:div {:style {:cursor "pointer"}
                            :onClick (fn [_] (Yzma.frontend.easy/push-state :rc-settings-page))} "settings"])
                  (Kuzco.core/as-element [:a {:href (Yzma.frontend.easy/href :rc-settings-page)} "settings"])
                  :key :rc-settings-page
                  :icon nil}]
         #_[{:label "program"
             :key "program"
             :icon nil
             :children []}]}]
       (when-let [match @matchA]
         [(-> match :data :view) match stateA])])
    #_[:<>

       [:ul
        [:li [:a {:href (Yzma.frontend.easy/href :rc-main-page)} "game"]]
        [:li [:a {:href (Yzma.frontend.easy/href :rc-settings-page)} "settings"]]]
       (when-let [match @matchA]
         [(-> match :data :view) match stateA])]))

(defn ui-process
  [{:keys [Pacha-dom-root
           matchA
           stateA]
    :as opts}]
  (let [history (Yzma.frontend.easy/start!
                 (Yzma.frontend/router
                  ["/"
                   [""
                    {:name :rc-main-page
                     :view rc-main-page
                     :controllers [{:start (fn [_]
                                             (js/console.log "start rc-main-page"))
                                    :stop (fn [_]
                                            (js/console.log "stop rc-main-page"))}]}]

                   ["tournament"
                    {:name :rc-tournament-page
                     :view rc-tournament-page
                     :controllers [{:start (fn [_]
                                             (js/console.log "start rc-tournament-page"))
                                    :stop (fn [_]
                                            (js/console.log "stop rc-tournament-page"))}]}]
                   ["setting"
                    {:name :rc-settings-page
                     :view rc-settings-page
                     :controllers [{:start (fn [_]
                                             (js/console.log "start rc-settings-page"))
                                    :stop (fn [_]
                                            (js/console.log "stop rc-settings-page"))}]}]]
                  {:data {:controllers [{:start (fn [_]
                                                  (js/console.log "start program"))
                                         :stop (fn [_]
                                                 (js/console.log "stop program"))}]
                          :coercion Yzma.coercion.spec/coercion}})
                 (fn [new-match]
                   (swap! matchA (fn [old-match]
                                   (if new-match
                                     (assoc new-match :controllers (Yzma.frontend.controllers/apply-controllers (:controllers old-match) new-match))))))
                 {:use-fragment false})]
    (goog.events/unlistenByKey (:click-listen-key history))
    (goog.events/listen js/document goog.events.EventType.CLICK
                        (fn [event]
                          (when-let [element (Yzma.frontend.history/closest-by-tag
                                              (Yzma.frontend.history/event-target event) "a")]
                            (let [uri (.parse goog.Uri (.-href element))]
                              (when (Yzma.frontend.history/ignore-anchor-click? (.-router history) event element uri)
                                (.preventDefault event)
                                (let [path (str (.getPath uri)
                                                (when (.hasQuery uri)
                                                  (str "?" (.getQuery uri)))
                                                (when (.hasFragment uri)
                                                  (str "#" (.getFragment uri))))]
                                  (.pushState js/window.history nil "" path)
                                  (Yzma.frontend.history/-on-navigate history path)))))) true)


    #_(Yzma.frontend.easy/push-state :rc-main-page)
    (.render Pacha-dom-root (Kuzco.core/as-element [rc-current-page matchA stateA]))))
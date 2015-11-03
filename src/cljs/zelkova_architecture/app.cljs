(ns zelkova-architecture.app
  (:require [reagent.core :as r]
            [jamesmacaulay.zelkova.signal :as z]
            [jamesmacaulay.zelkova.impl.signal :as zimpl]  
            [cljs.core.async :as async]
            [zelkova-architecture.counter :as counter]))

(enable-console-print!)

;; ============================================================================
;; ROOT
;;
;; Elm has some boilerplate for this in the form of Start App.

;; A channel with all the updates. Like elm's mailbox.
(def address (async/chan))

;; Contains the current state; merges initial model with updates.
(def model (z/foldp (fn [action state] (action state))
                    counter/initial-model
                    (z/input identity ::updates address)))

;; Merge current state with view.
(def main-signal (z/map (fn [m]
                          (counter/view address m))
                        model))

;; Convert the current view to atom so reagent can react.
(def dom-atom
  (let [live-graph (z/spawn main-signal)]
    (z/pipe-to-atom live-graph
                    (r/atom (zimpl/init live-graph)))))

;; Tie the atom, containing the dom tree in with Reagent.
(defn root-component [] @dom-atom)

(defn init []
  (r/render-component [root-component]
                      (.getElementById js/document "container")))

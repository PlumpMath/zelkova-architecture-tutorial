(ns zelkova-architecture.counter
  (:require [reagent.core :as r]
            [cljs.core.async :as async]))

;; ============================================================================
;; MODEL

(def initial-model 0)

;; ============================================================================
;; UPDATE

(defn send-action!
  [address f & args]
  (async/put! address (fn [model]
                        (apply f model args))))

(defn increase [state]
  (inc state))

(defn decrease [state]
  (dec state))

;; ============================================================================
;; VIEW

(defn view [address model]
  [:div
   [:button.inline {:on-click #(send-action! address decrease)} "-"]
   [:div.inline model]
   [:button.inline {:on-click #(send-action! address increase)} "+"]])

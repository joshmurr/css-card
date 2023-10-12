(ns app.card
  [:require [reagent.core :as r]
   [cljs.core.async :refer [go-loop pub sub chan <! put!]]])

(defonce msg-ch (chan 1))
(defonce msg-bus (pub msg-ch ::type))

(defn dispatch!
  ([type] (dispatch! type nil))
  ([type payload]
   (put! msg-ch {::type type
                 ::payload payload})))

(def initial-state
  {:pos {:x 0 :y 0}
   :percent {:x 0 :y 0}
   :center {:x 0 :y 0}})

(defn update-root-css [key val]
  (.. js/document -documentElement -style (setProperty (name key) val)))

(defn listen-for-pos [state]
  (let [changed (chan)]
    (sub msg-bus ::movement changed)
    (go-loop []
      (let [movement (::payload (<! changed))
            {:keys [pos center]} movement]
        (reset! state movement)
        (update-root-css :--pointer-x (str (:x pos) "px"))
        (update-root-css :--pointer-y (str (:y pos) "px"))
        (update-root-css :--rotate-x (str (/ (* -1 (:x center)) 3.5) "deg"))
        (update-root-css :--rotate-y (str (/ (:y center) 2) "deg"))
        (recur)))))

(defn clamp [v lb up]
  (min (max lb v) up))

(defn card [title body]
  [:div.card.card__rotator
   {:on-mouse-move (fn [e]
                     (let [elem (.. e -target)
                           rect (.getBoundingClientRect elem)
                           x (- (.. e -clientX) (.. rect -left))
                           y (- (.. e -clientY) (.. rect -top))
                           pos {:x x :y y}
                           percent {:x (clamp (* (/ 100 (.. rect -width)) x) 0 100)
                                    :y (clamp (* (/ 100 (.. rect -height)) y) 0 100)}
                           center {:x (- (:x percent) 50)
                                   :y (- (:y percent) 50)}]
                       (dispatch! ::movement {:pos pos :percent percent :center center})))}
   [:div.card__front
    [:div.card__glare
     [:h3.card-title {:style {:pointer-events "none"}} title]
     [:p.card-body {:style {:pointer-events "none"}} body]]]])

(defn output []
  (let [state (r/atom initial-state)]
    (listen-for-pos state)
    (fn []
      (let [{:keys [pos percent center]} @state]
        [:div.output
         [:pre "x: " (:x pos) " y: " (:y pos)]
         [:pre "x%: " (:x percent) " y%: " (:y percent)]
         [:pre "xc: " (:x center) " yc: " (:y center)]]))))

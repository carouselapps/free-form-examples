;;;; Copyright © 2015-2017 José Pablo Fernández Silva

(ns free-form-examples.routing
  (:require [reagent.ratom :as ratom :include-macros true]
            [re-frame.core :as re-frame]
            [clojure.set :refer [rename-keys]]
            [domkm.silk :as silk]
            [pushy.core :as pushy]))

(def routes (silk/routes [[:home [[]]]
                          [:reagent-bootstrap-3 [["reagent" "bootstrap-3"]]]
                          [:reagent-bootstrap-3-horizontal [["reagent" "bootstrap-3" "horizontal"]]]
                          [:reagent-bootstrap-3-inline [["reagent" "bootstrap-3" "inline"]]]
                          [:reagent-plain [["reagent" "plain"]]]
                          [:re-frame-bootstrap-3 [["re-frame" "bootstrap-3"]]]
                          [:re-frame-bootstrap-3-horizontal [["re-frame" "bootstrap-3" "horizontal"]]]
                          [:re-frame-bootstrap-3-inline [["re-frame" "bootstrap-3" "inline"]]]
                          [:re-frame-plain [["re-frame" "plain"]]]
                          [:re-frame-state [["re-frame" "state"]]]]))

(defn sanitize-silk-keywords [matched-route]
  (rename-keys matched-route {:domkm.silk/name    :name
                              :domkm.silk/pattern :pattern
                              :domkm.silk/routes  :routes
                              :domkm.silk/url     :url}))

(defn parse-path [path]
  (sanitize-silk-keywords (silk/arrive routes path)))

(defn routing-event [matched-route]
  [:set-current-route matched-route])

(defn dispatch-route [parsed-path]
  (re-frame/dispatch (routing-event parsed-path)))

(def history (atom nil))

(defn start! []
  (when (nil? @history)
    (reset! history (pushy/pushy dispatch-route parse-path)))
  (pushy/start! @history))

(def url-for (partial silk/depart routes))

(defn redirect-to [& args]
  (when @history
    (let [path (apply url-for args)
          self-redirect (= path (pushy/get-token @history))]
      (pushy/set-token! @history path)
      (when self-redirect                                   ; If we are re-directing to itself, we need to re-trigger routing manually.
        (when-let [parsed-path (parse-path path)]
          (dispatch-route parsed-path))))))

(re-frame/reg-event-db
  :redirect-to
  (fn [db [& args]]
    (apply redirect-to (drop 1 args))
    db))

(defmulti display-page :name)
(defmethod display-page :default [_current-route db]
  db)

(re-frame/reg-event-db
  :set-current-route
  (fn [db [_name current-route]]
    (display-page current-route (assoc db :current-route current-route))))

(re-frame/reg-sub-raw
  :current-route
  (fn [db _]
    (ratom/reaction (:current-route @db))))

;;;; Copyright © 2015, 2016 José Pablo Fernández Silva, All rights reserved.

(ns free-form-examples.forms.reagent.bootstrap-3-inline
  (:require [reagent.core :as reagent]
            [free-form.core :as free-form]
            [free-form-examples.layout :as layout]))

(defmethod layout/pages :reagent-bootstrap-3-inline [_]
  (let [data (reagent/atom {})]
    (fn [_]
      [:div
       [layout/source-code-button "reagent/bootstrap_3_inline.cljs"]
       [:h1 "Reagent Bootstrap 3 Inline"]
       [free-form/form {} {} (fn [keys value] (swap! data #(assoc-in % keys value)))
        [:form.form-inline {:noValidate        true
                            :free-form/options {:mode :bootstrap-3}}
         [:free-form/field {:type        :text
                            :key         :text
                            :label       "Text"
                            :placeholder "placeholder"}] " "
         [:free-form/field {:type        :email
                            :key         :email
                            :label       "Email"
                            :placeholder "placeholder@example.com"}] " "
         [:free-form/field {:type  :password
                            :label "Password"
                            :keys  [:password]}] " "
         [:free-form/field {:type    :select
                            :label   "Select"
                            :key     :select
                            :options [:dog "Dog"
                                      :cat "Cat"
                                      :squirrel "Squirrel"
                                      :giraffe "Giraffe"]}] " "
         [:free-form/field {:type    :select
                            :label   "Select with group"
                            :key     :select-with-group
                            :options ["Numbers" [:one "One"
                                                 :two "Two"
                                                 :three "Three"
                                                 :four "Four"]
                                      "Letters" [:a "A"
                                                 :b "B"
                                                 :c "C"
                                                 :d "D"]]}] " "
         [:free-form/field {:type  :textarea
                            :label "Text area"
                            :key   :textarea}]
         [:button.btn.btn-primary {:type :submit} "Button"]]]
       [layout/state @data]])))

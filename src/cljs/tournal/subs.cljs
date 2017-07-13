(ns tournal.subs
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :as re-frame]
              [tournal.db :as db]))

(re-frame/reg-sub
 :name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 :years
 (fn [db]
   (get-in db [:date-finder :years])))

(re-frame/reg-sub
 :selected-year
 (fn [db]
   (get-in db [:date-finder :selection :year])))

(re-frame/reg-sub
 :months
 (fn [db]
   (get-in db [:date-finder :months])))

(re-frame/reg-sub
 :selected-month
 (fn [db]
   (get-in db [:date-finder :selection :month])))

(re-frame/reg-sub
 :days
 (fn [db]
   (get-in db [:date-finder :days])))

(re-frame/reg-sub
 :selected-day
 (fn [db]
   (get-in db [:date-finder :selection :day])))

(re-frame/reg-sub
 :path
  :<- [:selected-year]
  :<- [:selected-month]
  :<- [:selected-day]
 (fn [selected-values  _]
   (clojure.string/join "/" selected-values)))

(re-frame/reg-sub
 :files
 (fn [db]
   (get-in db [:files])))

(re-frame/reg-sub
 :txts
 (fn [db]
   (get-in db [:txts])))

(re-frame/reg-sub
 :imgs
 (fn [db]
   (filter #(clojure.string/ends-with? (clojure.string/lower-case %) ".jpg")
           (get-in db [:files]))))

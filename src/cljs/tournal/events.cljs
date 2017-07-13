(ns tournal.events
  (:require [re-frame.core :as re-frame]
            [day8.re-frame.http-fx]
            [ajax.core     :as ajax]
            [cljs.reader :as reader]
            [tournal.db :as db]))

(re-frame/reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/reg-event-fx
 :folder-years-result
 (fn [{:keys [db]} [_ result]]
   (let [dir (reader/read-string result)]
     {:db 
      (-> db
          (assoc :txts [])
          (assoc-in [:date-finder :years]
                    (set (:folders dir)))
          (assoc-in [:files]
                    (get dir :files nil)))})))

(re-frame/reg-event-fx
 :folder-months-result
 (fn [{:keys [db]} [_ result]]
   (let [dir (reader/read-string result)]
     {:db (-> db
             (assoc :txts [])
             (assoc-in [:date-finder :months]
                       (set (:folders dir)))
             (assoc-in [:files]
                       (get dir :files nil)))})))

(re-frame/reg-event-fx
 :folder-days-result
 (fn [{:keys [db]} [_ result]]
   (let [dir (reader/read-string result)]
     (println (prn-str dir))
     {:db (-> db
              (assoc :txts [])
              (assoc-in [:date-finder :days]
                        (set (:folders dir)))
              (assoc-in [:files]
                        (get dir :files nil)))
      :dispatch (mapv (fn [filename] [:load-txt filename])
                      (get dir :files [])) ;;todo filter files by extension
      })))

(defn txt? [filename]
  (clojure.string/ends-with? filename ".txt")
  )

(re-frame/reg-event-fx
 :folder-day-result
 (fn [{:keys [db]} [_ result]]
   (let [dir (reader/read-string result)]
     {:db (assoc-in db [:files]
                      (:files dir))
      :dispatch-n (mapv (fn [filename] [:load-txt filename])
                        (filter txt? (get dir :files []))
                        )
      })))

(re-frame/reg-event-db
 :bad-http-result
 (fn [db [_ error]]
   (println
    (pr-str error))
   db))

(re-frame/reg-event-fx
 :load-years
 (fn [{:keys [db]} _]
   {:http-xhrio {:method          :get
                 :uri             "journal/.dir.edn"
                 :timeout         8000
                 :response-format (ajax/raw-response-format)
                 :on-success      [:folder-years-result]
                 :on-failure      [:bad-http-result]}}))

(re-frame/reg-event-fx
 :load-months
 (fn [{:keys [db]} [_ year]]
   {:http-xhrio {:method          :get
                 :uri             (str "journal/" year "/.dir.edn")
                 :timeout         8000
                 :response-format (ajax/raw-response-format)
                 :on-success      [:folder-months-result]
                 :on-failure      [:bad-http-result]}}))

(re-frame/reg-event-fx
 :load-days
 (fn [{:keys [db]} [_ month]]
   {:http-xhrio {:method          :get
                 :uri             (str "journal/"
                                       (get-in db [:date-finder :selection :year])
                                       "/" month "/.dir.edn")
                 :timeout         8000
                 :response-format (ajax/raw-response-format)
                 :on-success      [:folder-days-result]
                 :on-failure      [:bad-http-result]}}))

(re-frame/reg-event-fx
 :load-day
 (fn [{:keys [db]} [_ day]]
   {:http-xhrio {:method          :get
                 :uri             (str "journal/"
                                       (get-in db [:date-finder :selection :year])
                                       "/"
                                       (get-in db [:date-finder :selection :month])
                                       "/"
                                       day
                                       "/.dir.edn")
                 :timeout         8000
                 :response-format (ajax/raw-response-format)
                 :on-success      [:folder-day-result]
                 :on-failure      [:bad-http-result]}}))

(re-frame/reg-event-fx
 :load-txt
 (fn [{:keys [db]} [_ filename]]
   {:http-xhrio {:method          :get
                 :uri             (str "journal/"
                                       (get-in db [:date-finder :selection :year])
                                       "/"
                                       (get-in db [:date-finder :selection :month])
                                       "/"
                                       (get-in db [:date-finder :selection :day])
                                       "/"
                                       filename)
                 :timeout         8000
                 :response-format (ajax/raw-response-format)
                 :on-success      [:txts-result filename]
                 :on-failure      [:bad-http-result]}}))

(re-frame/reg-event-fx
 :txts-result
 (fn [{:keys [db]} [_  filename txt]]
   {:db
    (update db :txts conj [filename txt])
    }))


(re-frame/reg-event-fx
 :select-month
 (fn [{:keys [db]} [_ month]]
   {:db
    (-> db
        (assoc-in [:date-finder :selection :month]
                  month)
        (assoc-in [:date-finder :selection :day]
                  nil))
    :dispatch [:load-days month]}))

(re-frame/reg-event-fx
 :select-year
 (fn [{:keys [db]} [_ year]]
   {:db
    (-> db
        (assoc-in [:date-finder :selection :year]
                  year)
        (assoc-in [:date-finder :selection :month]
                  nil)
        (assoc-in [:date-finder :selection :day]
                  nil))
    :dispatch [:load-months year]}))



(re-frame/reg-event-fx
 :select-day
 (fn [{:keys [db]} [_ day]]
   {:db
    (assoc-in db [:date-finder :selection :day]
              day)
    :dispatch [:load-day day]}))

(comment
  (re-frame/dispatch [:load-years])
  (println (prn-str re-frame.db/app-db))
  (println "hello")
  
  (re-frame/dispatch [:select-year "2016"])

  (re-frame/dispatch [:load-txt "hola.txt"])
  )

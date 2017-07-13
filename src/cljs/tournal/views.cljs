(ns tournal.views
  (:require [re-frame.core :as re-frame]))

(defn list-selector [all selectable selected on-click-fn]
  [:div {:style {:display "flex"
                 :flex-wrap "wrap"
                 :justify-content "center"
                 :background-color "#f3f3f3"
                 :margin "5px"}}
   (map (fn [val] [:div
                   {:on-click #(on-click-fn val)
                    :style {:margin "5px 1px"
                            :padding "8px"
                            :color
                            (if (selectable val) "#333" "#aaa")
                            :background-color (if (= selected val) "yellow" "white")
                            :border "1px solid #ccc"}}  val])
        all)])


(defn img [file]
  ;todo on click increase size
  [:img {:style {:width "200" :margin "5"}
         :src (str "journal/" @(re-frame/subscribe [:path]) "/" file)}])

(defn txt [[filename content]]
  [:div
   [:h2 filename]
   [:div (map (fn [line] [:p line]) (clojure.string/split-lines content))]]
  )

(defn imgs []
  [:div
   (map img @(re-frame/subscribe [:imgs]))
   ]
  )

(defn txts []
  [:div
   (map txt @(re-frame/subscribe [:txts]))
   ]
  )

(defn two-digit-str [i]
  (if (> i 9) (str i) (str "0" i)))

(defn main-panel []
  [:div
   [list-selector (map str (range 2000 2018))
    @(re-frame/subscribe [:years])
    @(re-frame/subscribe [:selected-year])
    #(re-frame/dispatch [:select-year %])
    ]
   [list-selector (map two-digit-str (range 1 13))
    @(re-frame/subscribe [:months])
    @(re-frame/subscribe [:selected-month])
    #(re-frame/dispatch [:select-month %])]
   [list-selector (map two-digit-str (range 1 32))
    @(re-frame/subscribe [:days])
    @(re-frame/subscribe [:selected-day])
    #(re-frame/dispatch [:select-day %])]
   ;;[:div (prn-str @(re-frame/subscribe [:files]))]
   [txts]
   [imgs]
   ])


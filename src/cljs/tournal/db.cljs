(ns tournal.db
  (:require [clojure.spec.alpha :as s]))

(s/def ::year (s/and string? #(< 1900 (int %) 2200)))
(s/def ::month (s/and string? #(< 0 (int %) 13)))
(s/def ::day (s/and string? #(< 0 (int %) 32)))
(s/def ::years (s/coll-of ::year))
(s/def ::months (s/coll-of ::month))
(s/def ::days (s/coll-of ::day))
(s/def ::selection (s/keys :opt [::year
                                 ::month
                                 ::day]))
(s/def ::date-finder (s/keys :req [::years
                                   ::months
                                   ::days
                                   ::selection]))


(def default-db
  {:name "re-frame hola"
   :date-finder {:years #{"2014" "2015" "2016" "2017"}
                 :months #{"1" "8"}
                 :days #{"1" "22"}
                 :selection {:year nil
                             :month nil
                             :day nil}}
   :files []
   :txts [["welcome" "select year, month and day"]]})

(comment



  (s/valid? ::years ["1999"])

  (s/valid? ::date-finder {::years ["2017"]
                           ::months ["02"]
                           ::days ["01"]
                           ::selection {::year "2017"}
                           })

  (s/valid? ::date-finder {::years ["2017"]
                           ::months ["02"]
                           ::days ["01"]
                           ::selection {}
                           })
  )

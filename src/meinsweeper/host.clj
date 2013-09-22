(ns meinsweeper.host
  (:require
     [clojure.core.logic :as lg]
;    [meinsweeper.ai.facts :as facts]
;    [meinsweeper.ai.constraints :as constraints]
;    [meinsweeper.ai.constraint-resolution :as resolutions]
    [meinsweeper.grid :refer :all]
;    [meinsweeper.ai.clicks :as clicks]
;

    ))

(lg/defrel underlying-vacancy coordinates _)
(lg/defrel underlying-mine coordinates _)
(lg/defrel neighborless-vacancy coordinates _)

(defn mine-neighbors [square mine-neighbors]
  )

(defn got-neighbors []
  (set (lg/run* [r c]
                (lg/fresh [mine-row mine-col]
                  (adjacent [r c] [mine-row mine-col])
                  (underlying-vacancy [r c] nil)
                  (underlying-mine [mine-row mine-col] nil)))))

(defn record-neighborless-vacancies []
  (let [vacancies (set (lg/run* [square] (underlying-vacancy square nil)))
        neighborless-vacancies (clojure.set/difference vacancies (got-neighbors))]
   (doseq [square neighborless-vacancies]
    (lg/fact neighborless-vacancy square nil))))


(def connected?
  (lg/tabled
    [target current-candidate]
    (underlying-vacancy current-candidate nil)
    (lg/conde
      [(adjacent target current-candidate)]
      [(lg/fresh
         [intermediate-row intermediate-col]
         (underlying-vacancy [intermediate-row intermediate-col] nil)
         (adjacent [intermediate-row intermediate-col] current-candidate)
         (neighborless-vacancy [intermediate-row intermediate-col] nil)
         (connected? target [intermediate-row intermediate-col]))])))

(defn expand-for-click [click-location]
  (record-neighborless-vacancies)
  (set (lg/run* [row col]
                  (connected? click-location [row col]))))

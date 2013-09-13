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

(defn no-mine-neighbors [square]
(let [mine-neighbors (lg/run* [r c]
                       (on-grid square 10 10)
                       (underlying-mine [r c] nil)
                       (close [r c] square))]
  (lg/emptyo mine-neighbors)))


(def connected?
  (lg/tabled [target candidate]
     (lg/all
       (underlying-vacancy candidate nil)
       (no-mine-neighbors target)
       (lg/conde
         [(close target candidate)]
         [(lg/fresh [intermediate-row intermediate-col]
                    (underlying-vacancy [intermediate-row intermediate-col] nil)
                    (close [intermediate-row intermediate-col] target)
                    (connected? [intermediate-row intermediate-col] candidate))]))))

(defn expand-for-click [click-location]
  (set (lg/run* [row col]
           (connected? click-location [row col]))))

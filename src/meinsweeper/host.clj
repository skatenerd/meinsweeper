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
                                (underlying-mine [r c] nil)
                                (on-grid square 10 10)
                                (adjacent [r c] square))]
    (lg/emptyo mine-neighbors)))


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
         (no-mine-neighbors [intermediate-row intermediate-col])
         (connected? target [intermediate-row intermediate-col]))])))

(defn expand-for-click [click-location]
  (set (lg/run* [row col]
                (connected? click-location [row col] ))))

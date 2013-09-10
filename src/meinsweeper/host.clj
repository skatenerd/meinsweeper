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

(def connected?
  (lg/tabled [target candidate]
     (lg/all
       (underlying-vacancy candidate nil)
       (lg/conde
         [(close target candidate)]
         [(lg/fresh [newrow newcol]
                    (underlying-vacancy [newrow newcol] nil)
                    (close [newrow newcol] target)
                    (connected? [newrow newcol] candidate))]))))

(defn for-expansion [click-location]
  (set (lg/run* [row col]
           (connected? click-location [row col]))))




(ns meinsweeper.ai.clicks
  (:require [meinsweeper.square-names :refer :all]
            [meinsweeper.grid :as grid]))

(declare novel-points click-representation)

(defn for-fixed-squares [fixed-squares grid]
  (let [grouped (group-by second (novel-points fixed-squares grid))]
    {:vacancies (set (map first (get grouped 0)))
     :mines (set (map first (get grouped 1)))} )
  )

(defn- novel-points [fixed-squares grid]
  (filter
    (fn [square]
      (= unknown (grid/square-at grid (key square))))
    fixed-squares))

(defn- click-representation [fixed-square]
  [(key fixed-square) ({1 mine 0 vacant} (val fixed-square))])


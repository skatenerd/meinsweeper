(ns meinsweeper.ai
  (:require [meinsweeper.ai.facts :as facts]
            [meinsweeper.ai.constraints :as constraints]
            [meinsweeper.ai.constraint-resolution :as resolutions]
            [meinsweeper.grid :refer :all]
            [meinsweeper.square-names :refer :all]
            [meinsweeper.ai.clicks :as clicks]))


(defn fixed-squares [rows cols]
  (let [constraints (constraints/from-facts rows cols)]
    (resolutions/for-constraints constraints)))

(defn fixed-squares-for-grid [grid]
  (facts/generate-facts-for grid)
  (fixed-squares (rows-count grid) (cols-count grid)))

(defn clicks-for [grid]
  (clicks/for-fixed-squares (fixed-squares-for-grid grid) grid))

(defn grouped-clicks-for [grid]
  (let [clicks (clicks-for grid)
        grouped (group-by val clicks)]
    {:vacancies (set (map first (vacant grouped)))
     :mines (set (map first (mine grouped)))}))

(defn guess-move [grid]
  (let [rows-count (count grid)
        cols-count (count (first grid))]
    {:vacancies #{[(rand-int rows-count) (rand-int cols-count)]}
     :mines #{}}))

(defn aggressive-moves [grid]
  (let [moves (clicks-for grid)]
    ;(prn "grid -> moves" grid moves)
    (if (every? empty? (vals moves))
      (guess-move grid)
      moves)))

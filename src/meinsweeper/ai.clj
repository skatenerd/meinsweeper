(ns meinsweeper.ai
  (:require [meinsweeper.ai.facts :as facts]
            [meinsweeper.ai.constraints :as constraints]
            [meinsweeper.ai.constraint-resolution :as resolutions]
            [meinsweeper.grid :refer :all]
            [meinsweeper.ai.clicks :as clicks]))


(defn fixed-squares [rows cols]
  (let [constraints (constraints/from-facts rows cols)]
    (resolutions/for-constraints constraints)))

(defn fixed-squares-for-grid [grid]
  (facts/generate-facts-for grid)
  (fixed-squares (rows-count grid) (cols-count grid)))

(defn clicks-for [grid]
  (clicks/for-fixed-squares (fixed-squares-for-grid grid) grid))


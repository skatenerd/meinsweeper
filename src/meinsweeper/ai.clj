(ns meinsweeper.ai
  (:require [clojure.core.logic :as lg]
            [clojure.core.logic.fd :as fd]
            [meinsweeper.square-names :refer :all]
            [meinsweeper.ai.facts :refer :all]
            [meinsweeper.ai.constraint :refer :all]
            [meinsweeper.ai.constraint-resolution :refer :all]
            [meinsweeper.ai.grid :refer :all]
            [meinsweeper.ai.clicks :as clicks]
            ))


(defn facts-to-fixed-squares [rows cols]
  (let [constraints (constraints rows cols)]
    (fixed-coordinate-values constraints)))

(defn grid-to-fixed-squares [grid]
  (generate-facts-for grid)
  (facts-to-fixed-squares (rows-count grid) (cols-count grid)))

(defn clicks-for [grid]
  (clicks/fixed-squares-to-clicks (grid-to-fixed-squares grid) grid))


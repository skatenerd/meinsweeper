(ns meinsweeper.ai.clicks
  (:require [meinsweeper.square-names :refer :all]
            [meinsweeper.grid :as grid]))

(declare known-points click-representation)

(defn for-fixed-squares [fixed-squares grid]
  (into {} (map click-representation (known-points fixed-squares grid))))

(defn- known-points [fixed-squares grid]
  (filter
    #(= unknown (grid/square-at grid (key %)))
    fixed-squares))

(defn- click-representation [fixed-square]
  [(key fixed-square) ({1 mine 0 vacant} (val fixed-square))])


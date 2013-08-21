(ns meinsweeper.ai.clicks
  (:require [meinsweeper.square-names :refer :all]
            [meinsweeper.ai.grid :as grid]))

(declare relevant-points click-representation)

(defn fixed-squares-to-clicks [fixed-squares grid]
  (into {} (map click-representation (relevant-points fixed-squares grid))))

(defn- relevant-points [fixed-squares grid]
  (filter
    #(= unknown (grid/square-at grid (key %)))
    fixed-squares))

(defn- click-representation [fixed-square]
  [(key fixed-square) ({1 mine 0 vacant} (val fixed-square))])


(ns meinsweeper.game-board
  (:require [meinsweeper.square-names :refer :all]))

(defn new-grid [rows cols mines]
  (repeat rows (repeat cols vacant))
  )

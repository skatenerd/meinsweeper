(ns meinsweeper.game-board
  (:require [clojure.core.logic :as lg]
            [clojure.core.logic.fd :as fd]
            [meinsweeper.square-names :refer :all]))

(defn mine-positions [rows cols mines]
  (let [row-positions (repeatedly mines #(rand-nth (range rows)))
        col-positions (repeatedly mines #(rand-nth (range cols)))]
    (map vector row-positions col-positions)
    )
  )


(defn new-grid [rows cols mines]
  (let [mineless (vec (repeat rows (vec (repeat cols vacant))))
        mine-positions (mine-positions rows cols mines)]
    (reduce
      #(update-in %1 %2 (fn [_] mine))
      mineless
      mine-positions)
    ))

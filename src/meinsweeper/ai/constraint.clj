(ns meinsweeper.ai.constraint
  (:require [clojure.core.logic :as lg]
            [meinsweeper.ai.facts :as facts]
            [meinsweeper.ai.grid :as grid]))

(declare numbered-square-constraints mine-square-constraints)

(defrecord Constraint [squares total-mines])

(defn new-constraint [squares total-mines] (Constraint. squares total-mines))

(defn constraints [rows cols]
  (clojure.set/union
    (numbered-square-constraints rows cols)
    (mine-square-constraints)))

(defn- mine-square-constraints []
  (let [mine-square-facts (lg/run* [square _]
                               (facts/mine-square square _))]
    (set
      (for [[square _] mine-square-facts]
        (new-constraint (set [square]) 1)))))

(defn- constraints-for-fact [rows cols mine-count-fact]
  (let [[square count] mine-count-fact
        neighbors (grid/neighbors rows cols square)]
    [(new-constraint (set neighbors) count)
     (new-constraint (set [square]) 0)]))


(defn- numbered-square-constraints [rows cols]
  (let [mine-count-facts (lg/run* [square count]
                               (facts/numbered-square square count))]
    (set (flatten (map #(constraints-for-fact rows cols %) mine-count-facts)))))

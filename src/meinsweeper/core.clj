(ns meinsweeper.core
  (:require [clojure.core.logic :as lg]
            [clojure.core.logic.fd :as fd]))

(declare same-at)

(def unknown :unknown)
(def mine :mine)

(def lvar-for-coordinate
  (memoize #(lg/lvar %)))

(defrecord Constraint [squares total-mines])
(defn new-constraint [squares total-mines] (Constraint. squares total-mines))

(defn constraint-lvars [constraint]
  (map lvar-for-coordinate (:squares constraint)))

(defn bind-to-zero-or-one [lvars]
  (lg/everyg #(fd/in % (fd/interval 0 1)) lvars))

(defn is-sum [list num]
  (lg/conde
    [(lg/== list []) (lg/== num 0)]
    [(lg/fresh [head tail tail-sum]
       (lg/conso head tail list)
       (is-sum tail tail-sum)
       (fd/+ tail-sum head num))]))

(defn zip-with [first second]
  (map vector first second))

(defn all-coordinates [constraints]
  (reduce clojure.set/union (map :squares constraints)))

(defn solution-description [all-coordinates coordinate-value-list]
   (into {}  (zip-with all-coordinates coordinate-value-list)))

(defn lvars-for-constraints [constraints]
  (map lvar-for-coordinate (all-coordinates constraints)))

(defn value-lists [constraints]
  (let [all-lvars (lvars-for-constraints constraints)]
    (lg/run* [q]
           (bind-to-zero-or-one all-lvars)
           (lg/everyg #(is-sum (constraint-lvars %) (:total-mines %)) constraints)
           (lg/== q all-lvars))))

(defn solve-constraints [constraints]
  (let [value-lists (value-lists constraints)
        all-coordinates (all-coordinates constraints)]
    (map #(solution-description all-coordinates %) value-lists)))

(defn fixed-coordinate-values [constraints]
  (let [solution-descriptions (solve-constraints constraints)
        fixed-coordinates (same-at solution-descriptions)]
    (select-keys (first solution-descriptions) fixed-coordinates)))

(defn all-equal [potentially-equal]
  (= 1 (count (distinct potentially-equal))))

(defn same-at [maps]
  (filter (fn [key]
            (let [elements-at-index (map #(get % key) maps)]
              (all-equal elements-at-index)))
          (keys (first maps))))


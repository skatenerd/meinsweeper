(ns meinsweeper.core
  (:require [clojure.core.logic :as lg]
            [clojure.core.logic.fd :as fd]))

(declare same-at)

(def unknown :unknown)
(def mine :mine)

(defn bind-to-zero-or-one [lvars]
  (lg/everyg #(fd/in % (fd/interval 0 1)) lvars))

(defn is-sum [list num]
  (lg/conde
    [(lg/== list []) (lg/== num 0)]
    [(lg/fresh [head tail tail-sum]
       (lg/conso head tail list)
       (is-sum tail tail-sum)
       (fd/+ tail-sum head num))]))

(def lvar-for-coordinate
  (memoize #(lg/lvar %)))

(defn zip-with [first second]
  (map vector first second))

(defn lvar-list-to-sum [constraints]
  (reduce
    (fn [bucket [coordinates-list sum]]
      (assoc
        bucket
        (map #(lvar-for-coordinate %) coordinates-list)
        sum
        ))
    {}
    constraints))

(defn all-coordinates [constraints]
  (distinct (apply concat (keys constraints))))

(defn solution-description [all-coordinates coordinate-value-list]
   (into {}  (zip-with all-coordinates coordinate-value-list)))

(defn lvars-for-constraints [constraints]
  (map lvar-for-coordinate (all-coordinates constraints)))

(defn value-lists [constraints]
  (let [sums-for-lvars (lvar-list-to-sum constraints)
        all-lvars (lvars-for-constraints constraints)]
    (lg/run* [q]
           (bind-to-zero-or-one all-lvars)
           (lg/everyg #(is-sum % (sums-for-lvars %)) (keys sums-for-lvars))
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


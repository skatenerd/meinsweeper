(ns meinsweeper.ai.constraint-resolution
  (:require [clojure.core.logic :as lg]
            [clojure.core.logic.fd :as fd]
            [meinsweeper.data-queries :as data-queries]))

(declare all-coordinates solve-constraints rule-following-solutions solution-summary lvars-for-all-coordinates bind-to-zero-or-one is-sum constraint-lvars)

(defn for-constraints [constraints]
  (let [solution-summarys (solve-constraints constraints)
        fixed-coordinates (data-queries/indices-of-equality solution-summarys)]
    (select-keys (first solution-summarys) fixed-coordinates)))

(defn- solve-constraints [constraints]
  (let [rule-following-solutions (rule-following-solutions constraints)
        all-coordinates (all-coordinates constraints)]
    (map #(solution-summary all-coordinates %) rule-following-solutions)))

(defn- rule-following-solutions [constraints]
  (let [all-lvars (lvars-for-all-coordinates constraints)]
    (lg/run* [q]
           (bind-to-zero-or-one all-lvars)
           (lg/everyg #(is-sum (constraint-lvars %) (:total-mines %)) constraints)
           (lg/== q all-lvars))))

(defn- bind-to-zero-or-one [lvars]
  (lg/everyg #(fd/in % (fd/interval 0 1)) lvars))

(defn- is-sum [list num]
  (lg/conde
    [(lg/== list []) (lg/== num 0)]
    [(lg/fresh [head tail tail-sum]
       (lg/conso head tail list)
       (is-sum tail tail-sum)
       (fd/+ tail-sum head num))]))

(defn- solution-summary [all-coordinates coordinate-value-list]
   (into {}  (map vector all-coordinates coordinate-value-list)))

(defn- all-coordinates [constraints]
  (reduce clojure.set/union (map :squares constraints)))

(def lvar-for-coordinate
  (memoize #(lg/lvar %)))

(defn- constraint-lvars [constraint]
  (map lvar-for-coordinate (:squares constraint)))

(defn- lvars-for-all-coordinates [constraints]
  (map lvar-for-coordinate (all-coordinates constraints)))

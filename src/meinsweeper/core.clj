(ns meinsweeper.core
  (:require [clojure.core.logic :as lg]
            [clojure.core.logic.fd :as fd]))
;;;bs

(defn rows-count [grid]
  (count grid))

(defn cols-count [grid]
  (count (first grid)))

;;;Solving, given constraints
(declare same-at)

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


;;;Producing constraints
(lg/defrel numbered-square coordinates count)
(lg/defrel mine-square coordinates ignoreme)



(defn neighbors [rows cols square]
  (let [[square-row square-col] square]
    (set (lg/run* [row col]
           (fd/in row (fd/interval 0 (dec rows)))
           (fd/in col (fd/interval 0 (dec cols)))
           (fd/in row (fd/interval (- square-row 1) (+ square-row 1)))
           (fd/in col (fd/interval (- square-col 1) (+ square-col 1)))))))

(defn constraints-for-fact [rows cols mine-count-fact]
  (let [[square count] mine-count-fact
        neighbors (neighbors rows cols square)]
    [(new-constraint (set neighbors) count)
     (new-constraint (set [square]) 0)]))

(defn numbered-square-constraints [rows cols]
  (let [mine-count-facts (lg/run* [square count]
                               (numbered-square square count))]
    (set (flatten (map #(constraints-for-fact rows cols %) mine-count-facts)))))

(defn mine-square-constraints []
  (let [mine-square-facts (lg/run* [square ignoreme]
                               (mine-square square ignoreme))]
    (set
      (for [[square _] mine-square-facts]
        (new-constraint (set [square]) 1)))))

(defn constraints [rows cols]
  (clojure.set/union
    (numbered-square-constraints rows cols)
    (mine-square-constraints)
    ))


;;;;;;;;winning
(defn facts-to-fixed-points [rows cols]
  (let [constraints (constraints rows cols)]
    (fixed-coordinate-values constraints)))

(def unknown :unknown)
(def mine :mine)
(def vacant :vacant)

(defn mine? [square]
  (= mine square))

(defn generate-fact-for [square row-idx col-idx]
  (cond (number? square)
        (lg/fact numbered-square [row-idx col-idx] square)
        (mine? square)
        (lg/fact mine-square [row-idx col-idx] true)))

(defn square-at [grid [row-idx col-idx]]
  ((grid row-idx) col-idx) )

(defn generate-facts-for [grid]
  (doall (for [row-idx (range (rows-count grid))
               col-idx (range (cols-count grid))]
           (let [square (square-at grid [row-idx col-idx])]
             (generate-fact-for square row-idx col-idx)))))

(defn grid-to-fixed-points [grid]
  (generate-facts-for grid)
  (facts-to-fixed-points (rows-count grid) (cols-count grid)))

(defn relevant-points [fixed-points grid]
  (filter
    #(= unknown (square-at grid (key %)))
    fixed-points))

(defn click-representation [fixed-point]
  [(key fixed-point) ({1 mine 0 vacant} (val fixed-point))])

(defn fixed-points-to-clicks [fixed-points grid]
  (into {} (map click-representation (relevant-points fixed-points grid))))

(defn clicks-for [grid]
  (fixed-points-to-clicks (grid-to-fixed-points grid) grid))

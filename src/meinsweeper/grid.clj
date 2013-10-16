(ns meinsweeper.grid
  (:require [clojure.core.logic :as lg]
            [meinsweeper.square-names :refer :all]
            [clojure.core.logic.fd :as fd]))

(declare adjacent on-grid flush flush-path-exists)

(defn square-at [grid [row-idx col-idx]]
  ((grid row-idx) col-idx) )

(defn neighbors [rows-count cols-count target-square]
  (set (lg/run* [row col]
                (on-grid [row col] rows-count cols-count)
                (adjacent [row col] target-square))))

(defn empty-grid [rows cols]
  (vec (for [row (range rows)]
    (vec (for [col (range cols)]
     unknown)))))

(defn rows-count [grid]
  (count grid))

(defn cols-count [grid]
  (count (first grid)))

(defn all-positions
  ([grid]
   (all-positions (rows-count grid) (cols-count grid)))
  ([rows cols]
  (for [row (range rows)
        col (range cols)]
    [row col])))



(defn on-grid
  ([square grid]
   (on-grid square (rows-count grid) (cols-count grid)))
  ([[row col] rows-count cols-count]
  (lg/all
    (fd/in row (fd/interval 0 (dec rows-count)))
    (fd/in col (fd/interval 0 (dec cols-count))))))

(defn adjacent [[row-1 col-1] [row-2 col-2]]
  (lg/all
    (fd/eq (>= 1 (- row-2 row-1)))
    (fd/eq (>= 1 (- row-1 row-2)))
    (fd/eq (>= 1 (- col-2 col-1)))
    (fd/eq (>= 1 (- col-1 col-2)))))

(defn- flush [[row-1 col-1] [row-2 col-2]]
  (lg/all
    (adjacent [row-1 col-1] [row-2 col-2])
    (lg/conde
      [(lg/== row-1 row-2)]
      [(lg/== col-1 col-2)])))

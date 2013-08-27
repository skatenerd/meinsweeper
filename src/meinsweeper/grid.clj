(ns meinsweeper.grid
  (:require [clojure.core.logic :as lg]
            [clojure.core.logic.fd :as fd]))

(defn square-at [grid [row-idx col-idx]]
  ((grid row-idx) col-idx) )

(defn on-grid [[row col] rows cols]
  (lg/all
    (fd/in row (fd/interval 0 (dec rows)))
    (fd/in col (fd/interval 0 (dec cols)))))

(defn close [[row-1 col-1] [row-2 col-2]]
  (lg/all
    (fd/in row-1 (fd/interval (- row-2 1) (+ row-2 1)))
    (fd/in col-1 (fd/interval (- col-2 1) (+ col-2 1)))))

(defn neighbors [rows cols square]
  (let [[square-row square-col] square]
    (set (lg/run* [row col]
                  (on-grid [row col] rows cols)
                  (close [row col] square)))))

(defn rows-count [grid]
  (count grid))

(defn cols-count [grid]
  (count (first grid)))


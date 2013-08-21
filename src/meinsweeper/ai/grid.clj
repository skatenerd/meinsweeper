(ns meinsweeper.ai.grid
  (:require [clojure.core.logic :as lg]
            [clojure.core.logic.fd :as fd]))

(defn square-at [grid [row-idx col-idx]]
  ((grid row-idx) col-idx) )

(defn neighbors [rows cols square]
  (let [[square-row square-col] square]
    (set (lg/run* [row col]
           (fd/in row (fd/interval 0 (dec rows)))
           (fd/in col (fd/interval 0 (dec cols)))
           (fd/in row (fd/interval (- square-row 1) (+ square-row 1)))
           (fd/in col (fd/interval (- square-col 1) (+ square-col 1)))))))

(defn rows-count [grid]
  (count grid))

(defn cols-count [grid]
  (count (first grid)))


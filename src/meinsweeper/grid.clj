(ns meinsweeper.grid
  (:require [clojure.core.logic :as lg]
            [clojure.core.logic.fd :as fd]))

(declare close on-grid flush flush-path-exists)

(defn square-at [grid [row-idx col-idx]]
  ((grid row-idx) col-idx) )

(defn neighbors [rows-count cols-count target-square]
  (set (lg/run* [row col]
                (on-grid [row col] rows-count cols-count)
                (close [row col] target-square))))

(defn flush-neighbors [rows-count cols-count target-square]
  (set (lg/run* [row col]
                (on-grid [row col] rows-count cols-count)
                (flush [row col] target-square))))

(defn flush-path-to [target grid]
  (lg/run*
    [row col]
    (flush-path-exists target [row col] grid)))

(def flush-path-exists
  (lg/tabled [target candidate grid]
    (on-grid candidate grid)
    (lg/fresh [row col]
              (flush [row col] candidate)
              (on-grid [row col] grid)
              (prn target)
              (lg/conde
                [(lg/all
                   (fd/eq (= row 0))
                   (fd/eq (= col 0)))]
                [(flush-path-exists [row col] target grid)])))


  )

(defn rows-count [grid]
  (count grid))

(defn cols-count [grid]
  (count (first grid)))

(defn- on-grid
  ([square grid]
   (on-grid square (rows-count grid) (cols-count grid)))
  ([[row col] rows-count cols-count]
  (lg/all
    (fd/in row (fd/interval 0 (dec rows-count)))
    (fd/in col (fd/interval 0 (dec cols-count))))))

(defn- close [[row-1 col-1] [row-2 col-2]]
  (lg/all
    (fd/eq (>= 1 (- row-2 row-1)))
    (fd/eq (>= 1 (- row-1 row-2)))
    (fd/eq (>= 1 (- col-2 col-1)))
    (fd/eq (>= 1 (- col-1 col-2)))))

(defn- flush [[row-1 col-1] [row-2 col-2]]
  (lg/all
    (close [row-1 col-1] [row-2 col-2])
    (lg/conde
      [(lg/== row-1 row-2)]
      [(lg/== col-1 col-2)])))

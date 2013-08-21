(ns meinsweeper.ai.facts
  (:require [clojure.core.logic :as lg]
            [meinsweeper.square-names :refer :all]
            [meinsweeper.ai.grid :as grid]))

(declare mine? generate-fact-for)

(lg/defrel numbered-square coordinates count)
(lg/defrel mine-square coordinates _)

(defn record-numbered-square [coordinates count]
  (lg/fact numbered-square coordinates count))

(defn record-mine-square [coordinates]
  (lg/fact mine-square coordinates true))

(defn generate-facts-for [grid]
  (doall (for [row-idx (range (grid/rows-count grid))
               col-idx (range (grid/cols-count grid))]
           (let [square (grid/square-at grid [row-idx col-idx])]
             (generate-fact-for square row-idx col-idx)))))

(defn- generate-fact-for [square row-idx col-idx]
  (cond (number? square)
        (lg/fact numbered-square [row-idx col-idx] square)
        (mine? square)
        (lg/fact mine-square [row-idx col-idx] true)))

(defn- mine? [square]
  (= mine square))


(ns meinsweeper.main-loop
  (:require
    [meinsweeper.host :refer :all]
    [meinsweeper.host.visualization :refer :all]
    [meinsweeper.square-names :refer :all]
    [meinsweeper.ai :refer :all]))


(defn take-while-and-n-more [pred n coll]
    (let [[head tail] (split-with pred coll)]
         (concat head (take n tail))))

(defn next-frame [current-frame rows cols]
  (let [grid (:grid current-frame)
        theory (:theory current-frame)
        moves-made (aggressive-moves grid)
        new-cpu-theory (merge-with clojure.set/union theory moves-made)
        new-grid (for-theory new-cpu-theory rows cols)]
    {:theory new-cpu-theory :grid new-grid}))

(defn game-sequence [mines rows cols]
  (build-game-facts mines rows cols)
  (let [initial-theory {:mines #{} :vacancies #{}}
        initial-grid (for-theory initial-theory rows cols)
        initial-frame {:theory initial-theory :grid initial-grid}]
    (take-while-and-n-more #(not (over? (:grid %))) 1 (iterate #(next-frame % rows cols) initial-frame))))

(defn go [mines rows cols]
  (doseq [frame (game-sequence mines rows cols)]
    (print-viewable-game (:grid frame))
    (println)))

(defn -main []
  (go 10 10 10))

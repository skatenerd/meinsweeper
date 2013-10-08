(ns meinsweeper.main-loop
  (:require
    [meinsweeper.host :refer :all]
    [meinsweeper.host.visualization :refer :all]
    [meinsweeper.square-names :refer :all]
    [meinsweeper.ai :refer :all]))


(defn go [mines rows cols iterations]
  (build-game-facts mines rows cols)
  (loop [cpu-theory {:mines #{} :vacancies #{}}
         iteration 0]
    (if (< iteration iterations)
      (let [game-as-grid (for-theory cpu-theory rows cols)
            moves-made (aggressive-moves game-as-grid)
            new-cpu-theory (merge-with clojure.set/union cpu-theory moves-made)]
        (print-viewable-game game-as-grid)
        (println "")
        (recur new-cpu-theory (inc iteration))))))

(defn -main []
  (go 10 10 10 10))

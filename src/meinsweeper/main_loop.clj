(ns meinsweeper.main-loop
  (:require
    [meinsweeper.host :refer :all]
    [meinsweeper.host.visualization :refer :all]
    [meinsweeper.square-names :refer :all]
    [meinsweeper.ai :refer :all]))

(defn aggressive-moves [game-as-grid rows cols]
  (let [moves (clicks-for game-as-grid)]
    (if (every? empty? (vals moves))
      {:vacancies #{[(rand-int rows) (rand-int cols)]}
       :mines #{}}
      moves)))

(defn new-exposed-squares [moves-made exposed-squares]
  (reduce
    (fn [all current]
      (clojure.set/union all (expand-for-click current)))
    exposed-squares
    moves-made))

(defn go [mines rows cols]
  (build-game-facts mines rows cols)
  (loop [cpu-theory {:mines #{} :vacancies #{}}
         iterations 0]
    (if (< iterations 10)
      (let [game-as-grid (for-theory cpu-theory rows cols)
            moves-made (aggressive-moves game-as-grid rows cols)
            new-cpu-theory (merge-with clojure.set/union cpu-theory moves-made)]
        (print-viewable-game game-as-grid)
        (println "")
        (recur new-cpu-theory (inc iterations))))))

(defn -main []
  (go 10 10 10))

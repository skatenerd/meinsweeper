(ns meinsweeper.main-loop
  (:require
    [meinsweeper.host :refer :all]
    [meinsweeper.square-names :refer :all]
    [meinsweeper.ai :refer :all]))

(defn aggressive-moves [exposed-squares rows cols]
  (let [game-state (state-map exposed-squares rows cols)
        moves (clicks-for game-state)]
    (prn "cpu moves were:  " moves)
    (if (empty? moves)
      {[(rand-int rows) (rand-int cols)] vacant}
      moves
      )))

(defn new-exposed-squares [moves-made exposed-squares]
  (reduce
    (fn [all current]
      (clojure.set/union all (expand-for-click current)))
    exposed-squares
    moves-made))

(defn go [mines rows cols]
  (build-game-facts mines rows cols)
  (loop [exposed-squares #{}
         iterations 0]
    (if (< iterations 10)
      (let [moves-made (aggressive-moves exposed-squares rows cols)
            new-exposed-squares (new-exposed-squares moves-made exposed-squares)]
        (print-viewable-game (viewable-game new-exposed-squares rows cols))
        (println "")
        (recur new-exposed-squares (inc iterations))
        ))))

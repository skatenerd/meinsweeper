(ns meinsweeper.game-board-spec
  (:require
    [meinsweeper.square-names :refer :all]
    [speclj.core :refer :all]
    [meinsweeper.game-board :refer :all]))


(describe
  "new game"
  (it "makes a grid with specified rows cols and no mines"
      (let [new-grid (new-grid 5 10 0)]
        (should= 5 (count new-grid))
        (should= 10 (count (first new-grid)))
        (should= (list vacant) (distinct (flatten new-grid)))
        ))


          )

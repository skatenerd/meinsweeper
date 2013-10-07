(ns meinsweeper.host.visualization-spec
  (:require [speclj.core :refer :all]
            [meinsweeper.square-names :refer :all]
            [meinsweeper.host.visualization :refer :all]))

(describe
  "printing"
  (it "prints the state of the game"
      (should=
        [["2" "*"]
         ["☐" " "]]
        (viewable-game
          [[2 mine]
           [unknown 0]]))))


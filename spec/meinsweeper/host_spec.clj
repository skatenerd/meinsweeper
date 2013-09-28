(ns meinsweeper.host-spec
  (:require [speclj.core :refer :all]
            [clojure.core.logic :as lg]
            [meinsweeper.square-names :refer :all]
            [meinsweeper.spec-helper :refer :all]
            [meinsweeper.host :refer :all]))

(describe
  "construction"
  (before
    (remove-all-facts))
  (it "randomly chooses positions for mines"
      (should= #{[0 0]} (random-mine-positions 1 1 1))
      (should= 5 (count (random-mine-positions 5 3 3)))
      ; (should= 5 (count (distinct (random-mine-positions 5 3 3))))

      )
  (it "generates facts to represent a new game"
      (build-game-facts 7 5 5)
      (let [mine-facts (set (lg/run* [square] (underlying-mine square nil)))]
        (should= 7 (count mine-facts)))
      (let [vacancy-facts (set (lg/run* [square] (underlying-vacancy square nil)))]
        (should= (- 25 7) (count vacancy-facts)))
      ))

(describe
  "queries for drawing"
  (before
    (remove-all-facts))
  (it "knows if a square has a mine"
      (build-game-facts 25 5 5)
      (should (mine? [2 2])))
  (it "knows if a square has no mine"
      (build-game-facts 0 5 5)
      (should-not (mine? [2 2])))
  (it "knows how many mine neighbors a square has"
      (lg/fact underlying-vacancy [0 0] nil)
      (lg/fact underlying-vacancy [1 1] nil)
      (lg/fact underlying-mine [1 0] nil)
      (lg/fact underlying-mine [0 1] nil)
      (should= 2 (neighbor-mine-count [0 0]))
      (should= 0 (neighbor-mine-count [9 9])))

  (it "builds a state-map to tell about exposed squares"
      (lg/fact underlying-vacancy [0 0] nil)
      (lg/fact underlying-vacancy [1 1] nil)
      (lg/fact underlying-mine [1 0] nil)
      (lg/fact underlying-mine [0 1] nil)
      (should=
        [[2 unknown]
         [mine unknown]]
        (state-map #{[0 0] [1 0]} 2 2)))

  (it "prints the state of the game"
      (lg/fact underlying-vacancy [0 0] nil)
      (lg/fact underlying-vacancy [1 1] nil)
      (lg/fact underlying-vacancy [2 2] nil)
      (lg/fact underlying-mine [1 0] nil)
      (lg/fact underlying-mine [0 1] nil)
      (should=
        [["2" "*" "☐"]
         ["*" "☐" "☐"]
         ["☐" "☐" " "]]
        (viewable-game #{[0 0] [1 0] [0 1] [2 2]} 2 2))
      (print-viewable-game (viewable-game #{[0 0] [1 0] [0 1] [2 2]} 2 2))))


(describe
  "integration"
  (before
    (remove-all-facts))

  (it "fills out a blocked-off area and doesnt go past wall of mines"
      (lg/fact underlying-mine [1 4] nil)
      (lg/fact underlying-mine [0 4] nil)
      (lg/fact underlying-vacancy [0 0] nil)
      (lg/fact underlying-vacancy [1 0] nil)
      (lg/fact underlying-vacancy [0 1] nil)
      (lg/fact underlying-vacancy [1 1] nil)
      (lg/fact underlying-vacancy [0 2] nil)
      (lg/fact underlying-vacancy [1 2] nil)
      (lg/fact underlying-vacancy [0 3] nil)
      (lg/fact underlying-vacancy [1 3] nil)
      (lg/fact underlying-vacancy [0 5] nil)
      (lg/fact underlying-vacancy [1 5] nil)


      (should=
        #{[1 0] [0 0] [1 1] [0 1] [1 2] [0 2] [1 3] [0 3]}
        (expand-for-click [[0 0] vacant])))

  (it "doesnt go past nub (two consecutive spaces on connecting path cannot be adjacent to mines)"
      (lg/fact underlying-mine [1 2] nil)
      (lg/fact underlying-vacancy [0 0] nil)
      (lg/fact underlying-vacancy [1 0] nil)
      (lg/fact underlying-vacancy [0 1] nil)
      (lg/fact underlying-vacancy [1 1] nil)
      (lg/fact underlying-vacancy [0 2] nil)
      (lg/fact underlying-vacancy [0 3] nil)
      (lg/fact underlying-vacancy [1 3] nil)

      (should= #{[1 0] [0 0] [1 1] [0 1]} (expand-for-click [[0 0] vacant])))
  )

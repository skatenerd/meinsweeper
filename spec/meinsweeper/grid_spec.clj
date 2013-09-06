(ns meinsweeper.grid-spec
  (:require [speclj.core :refer :all]
            [meinsweeper.square-names :refer :all]
            [meinsweeper.grid :refer :all]))


(describe
  "neighbors"
  (it "computes immediate neighbors"
      (should= #{[1 0] [0 0] [1 1] [0 1]} (neighbors 5 5 [0 0])))
  (it "computes flush neighbors"
      (should= #{[1 0] [0 0] [0 1]} (flush-neighbors 2 2 [0 0])))
  (it "neighbors connected by flush path"
      (let [grid [[vacant vacant vacant]
                  [vacant vacant vacant]
                  [mine   vacant vacant]]]
        (should= "hello!" (flush-path-to [0 0] grid))))
  )



(ns meinsweeper.ai.constraints-spec
  (:require [speclj.core :refer :all]
            [meinsweeper.ai.constraints :refer :all]
            [meinsweeper.spec-helper :refer :all]
            [meinsweeper.ai.facts :refer :all]
            ))

(describe
  "generate constraint from board"

  (before
    (remove-all-facts))

  (it "generates constraints from board dimensions + existing facts"
      (record-numbered-square [0 0] 2)
      (record-numbered-square [0 1] 3)

      (should= #{(new-constraint #{[0 0]} 0)
                 (new-constraint #{[0 1]} 0)
                 (new-constraint #{[0 0] [0 1] [1 0] [1 1]}  2)
                 (new-constraint #{[0 0] [0 1] [0 2] [1 0] [1 1] [1 2]} 3)}
               (from-facts 2 3)))


  (it "generates constraints from mines"
      (record-mine-square [0 1])
      (should= #{(new-constraint #{[0 1]} 1)}
               (from-facts 2 3))
      )
  )

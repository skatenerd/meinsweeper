(ns meinsweeper.ai-spec
  (:require [speclj.core :refer :all]
            [clojure.core.logic :as lg]
            [meinsweeper.square-names :refer :all]
            [meinsweeper.ai.facts :refer :all]
            [meinsweeper.spec-helper :refer :all]
            [meinsweeper.ai :refer :all]))
(describe
  "integration"
  (before
    (remove-all-facts))

  (it "facts -> fixed squares"
      (lg/fact numbered-square [0 0] 1)
      (lg/fact numbered-square [0 1] 2)
      (lg/fact numbered-square [0 2] 2)
      (lg/fact numbered-square [0 3] 1)

      (should= {
                [0 0] 0
                [0 1] 0
                [0 2] 0
                [0 3] 0
                [1 0] 0
                [1 1] 1
                [1 2] 1
                [1 3] 0}
               (fixed-squares 2 4)))

  (describe
    "grid input -> fixed squares"
    (it "1-2-2-1 case"
        (should= {[0 0] 0
                  [0 1] 0
                  [0 2] 0
                  [0 3] 0
                  [1 0] 0
                  [1 1] 1
                  [1 2] 1
                  [1 3] 0}
                 (fixed-squares-for-grid [[1       2       2       1]
                                         [unknown unknown unknown unknown]])


                 ))

    (it "acknowledges existing mines"
        (should= {
                  [0 0] 0
                  [0 1] 0
                  [0 2] 0
                  [1 0] 0
                  [1 1] 1
                  [1 2] 0
                  }
                 (fixed-squares-for-grid [[unknown 1    unknown]
                                         [unknown mine unknown]]))))

  (it "grid input -> clicks you should make"
      (should= {[1 0] vacant
                [1 1] mine
                [1 2] mine
                [1 3] vacant}
               (clicks-for [[1       2       2       1]
                            [unknown unknown unknown unknown]])))

  )


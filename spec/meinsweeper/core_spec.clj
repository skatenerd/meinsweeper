(ns meinsweeper.core-spec
  (:require [speclj.core :refer :all]
            [clojure.core.logic :as lg]
            [meinsweeper.core :refer :all]))

(defn remove-facts [relation]
  (let [facts (lg/run* [a b]
                       (relation a b)
                       )]
    (lg/retractions relation facts)
    ))

(defmacro remove-facts [relation n-args]
  (let [argvector (vec (repeatedly n-args gensym))]
    `(let [facts# (lg/run* ~argvector
                       (~relation ~@argvector))]
       (lg/retractions ~relation (vec facts#)))))

(describe
  "integration"
  (before
    (remove-facts numbered-square 2)
    (remove-facts mine-square 2))

  (it
    "finds values for coordinates fixed by constraints"
    (let [constraints-set #{
                            (new-constraint #{[0 0] [0 1]} 1)
                            (new-constraint #{[0 0] [0 1] [0 2]} 2)}]
      (should= {[0 2] 1} (fixed-coordinate-values constraints-set))))

  (it "gets constraints from board dimensions + existing facts"
      (lg/fact numbered-square [0 0] 2)
      (lg/fact numbered-square [0 1] 3)

      (should= #{
                 (new-constraint #{[0 0]} 0)
                 (new-constraint #{[0 1]} 0)
                 (new-constraint #{[0 0] [0 1] [1 0] [1 1]}  2)
                 (new-constraint #{[0 0] [0 1] [0 2] [1 0] [1 1] [1 2]} 3)}
               (constraints 2 3)))

  (it "generates constraints from mines"
      (lg/fact mine-square [0 1] true)
      (should= #{(new-constraint #{[0 1]} 1)}
               (constraints 2 3))
      )

  (it "solves 1-2-2-1 case, starting with facts"
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
               (facts-to-fixed-points 2 4)))

  (it "finds fixed points from 1-2-2-1 array input"
      (should= {
                [0 0] 0
                [0 1] 0
                [0 2] 0
                [0 3] 0
                [1 0] 0
                [1 1] 1
                [1 2] 1
                [1 3] 0}
               (grid-to-fixed-points [[1       2       2       1]
                                      [unknown unknown unknown unknown]])


               ))

  (it "learns from mines on board"
       (should= {
                 [0 0] 0;kill these
                 [0 1] 0
                 [0 2] 0
                 [1 0] 0;till here...
                 [1 1] 1
                 [1 2] 0
                 }
                (grid-to-fixed-points [[unknown 1    unknown]
                                       [unknown mine unknown]])))

  (it "tells you what clicks to make"
      (should= {[1 0] vacant
                [1 1] mine
                [1 2] mine
                [1 3] vacant}
               (clicks-for [[1       2       2       1]
                            [unknown unknown unknown unknown]])))


  )


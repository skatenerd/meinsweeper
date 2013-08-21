(ns meinsweeper.ai.constraint-resolution-spec
  (:require [speclj.core :refer :all]
            [meinsweeper.ai.constraint :refer :all]
            [meinsweeper.ai.constraint-resolution :refer :all]))

(describe
  "constraint resolution"
  (it "constraints -> fixed squares"
      (let [constraints-set #{(new-constraint #{[0 0] [0 1]} 1)
                              (new-constraint #{[0 0] [0 1] [0 2]} 2)}]
        (should= {[0 2] 1} (fixed-coordinate-values constraints-set)))))

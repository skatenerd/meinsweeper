(ns meinsweeper.core-spec
  (:require [speclj.core :refer :all]
            [meinsweeper.core :refer :all]))

(describe
  "integration"
  (it
    "finds values for coordinates fixed by constraints"
    (let [constraints {#{[0 0] [0 1]} 1
                       #{[0 0] [0 1] [0 2]} 2}]
      (should= {[0 2] 1} (fixed-coordinate-values constraints))
      ))
  )


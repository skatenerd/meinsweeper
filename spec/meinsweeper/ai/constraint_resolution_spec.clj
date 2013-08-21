(ns meinsweeper.ai.constraint-resolution-spec
  (:require [speclj.core :refer :all]
            [meinsweeper.ai.constraints :refer :all]
            [meinsweeper.ai.constraint-resolution :as resolutions]))

(describe
  "constraint resolution"
  (it "constraints -> fixed squares"
      (let [constraints-set #{(new-constraint #{[0 0] [0 1]} 1)
                              (new-constraint #{[0 0] [0 1] [0 2]} 2)}]
        (should= {[0 2] 1} (resolutions/for-constraints constraints-set)))))

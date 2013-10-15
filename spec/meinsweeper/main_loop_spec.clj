(ns meinsweeper.main-loop-spec
  (:require [speclj.core :refer :all]
            [meinsweeper.spec-helper :refer :all]
            [clojure.core.logic :as lg]
            [meinsweeper.host :refer :all]
            [meinsweeper.square-names :refer :all]
            [meinsweeper.main-loop :refer :all]
            [meinsweeper.ai :refer :all]))
(describe
  "interaction between host and ai"
  (before
    (remove-all-facts))
  (it "makes sequence of games"
      (lg/fact underlying-mine [0 0] nil)
      (lg/fact underlying-vacancy [0 1] nil)
      (lg/fact underlying-vacancy [1 0] nil)
      (lg/fact underlying-vacancy [1 1] nil)
      (let [intermediate-frame
            {:theory {:mines #{[0 0]} :vacancies #{[0 1]}}
             :grid [[mine 1]
                    [unknown unknown]]}
            next-frame (next-frame intermediate-frame 2 2)]
        (should= [[mine 1]
                  [1 1]]
                 (:grid next-frame)
                 )))
  )

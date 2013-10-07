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

  (it "grid input -> grouped clicks you should make"
      (should=
        {:vacancies #{[1 0] [1 3]}
         :mines #{[1 1] [1 2]}}
        (clicks-for [[1       2       2       1]
                     [unknown unknown unknown unknown]])))

  )


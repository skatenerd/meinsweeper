(ns meinsweeper.host-spec
  (:require [speclj.core :refer :all]
            [clojure.core.logic :as lg]
;            [meinsweeper.square-names :refer :all]
;            [meinsweeper.ai.facts :refer :all]
            [meinsweeper.spec-helper :refer :all]
            [meinsweeper.host :refer :all]

            ))
(describe
  "integration"
  (before
    (remove-all-facts))

  (it "fills out a blocked-off area"
      (lg/fact underlying-mine [1 1] 2)
      (lg/fact underlying-mine [0 1] nil)
      (lg/fact underlying-vacancy [0 0] nil)
      (lg/fact underlying-vacancy [1 0] nil)
      (lg/fact underlying-vacancy [0 2] nil)
      (lg/fact underlying-vacancy [1 2] nil)

      (should= #{[0 0] [1 0]} (for-expansion [0 0]))))

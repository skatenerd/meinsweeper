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

  (xit "doesnt grow for stuff adjacent to mines"
      (lg/fact underlying-mine [1 1] nil)
      (lg/fact underlying-mine [0 1] nil)
      (lg/fact underlying-vacancy [0 0] nil)
      (lg/fact underlying-vacancy [1 0] nil)
      (lg/fact underlying-vacancy [0 2] nil)
      (lg/fact underlying-vacancy [1 2] nil)

      (should= #{} (expand-for-click [0 0])))

  (it "fills out a blocked-off area 2"
      (lg/fact underlying-mine [1 4] nil)
      (lg/fact underlying-mine [0 4] nil)
      (lg/fact underlying-vacancy [0 0] nil)
      (lg/fact underlying-vacancy [1 0] nil)
      (lg/fact underlying-vacancy [0 1] nil)
      (lg/fact underlying-vacancy [1 1] nil)
      (lg/fact underlying-vacancy [0 2] nil)
      (lg/fact underlying-vacancy [1 2] nil)
      (lg/fact underlying-vacancy [0 3] nil)
      (lg/fact underlying-vacancy [1 3] nil)

      (should= #{[1 0] [0 0] [1 1] [0 1] [1 2] [0 2] [1 3] [0 3]} (expand-for-click [0 0])))

  (it "doesnt go past nub"
      (lg/fact underlying-mine [1 2] nil)
      (lg/fact underlying-vacancy [0 0] nil)
      (lg/fact underlying-vacancy [1 0] nil)
      (lg/fact underlying-vacancy [0 1] nil)
      (lg/fact underlying-vacancy [1 1] nil)
      (lg/fact underlying-vacancy [0 2] nil)
      (lg/fact underlying-vacancy [0 3] nil)
      (lg/fact underlying-vacancy [1 3] nil)

      (should= #{[1 0] [0 0] [1 1] [0 1]} (expand-for-click [0 0])))

;  (it "is smart about finding mine-neighbors"
;      (lg/fact underlying-mine [1 3] nil)
;      (lg/fact underlying-mine [0 3] nil)
;      (lg/fact underlying-vacancy [0 0] nil)
;      (lg/fact underlying-vacancy [1 0] nil)
;      (lg/fact underlying-vacancy [0 1] nil)
;      (lg/fact underlying-vacancy [1 1] nil)
;      (lg/fact underlying-vacancy [0 2] nil)
;      (lg/fact underlying-vacancy [1 2] nil)
;      (should= "i dont know yet" (no-mine-neighbors [0 1]))
;
;
;      )

  )

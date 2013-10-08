(ns meinsweeper.main-loop-spec
  (:require [speclj.core :refer :all]
            [meinsweeper.spec-helper :refer :all]
            [meinsweeper.main-loop :refer :all]))
(describe
  "interaction between host and ai"
  (before
    (remove-all-facts))
  (xit "plays 10 rounds"
      (go 5 5 5 10)))

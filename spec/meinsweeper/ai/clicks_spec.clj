(ns meinsweeper.ai.clicks-spec
  (:require [speclj.core :refer :all]
            [meinsweeper.square-names :refer :all]
            [meinsweeper.ai.clicks :refer :all]))

(describe
  "telling what clicks you should make"
  (it "builds a map telling you vacancies and mines"
      (let [grid [[unknown unknown unknown]]
            fixed-squares {[0 0] 0 [0 1] 0 [0 2] 1}]
        (should=
          {:mines #{[0 2]} :vacancies #{[0 0] [0 1]}}
          (for-fixed-squares fixed-squares grid))))

  (it "does not suggest clicks for points that are already cleared"
      (let [grid [[mine]]
            fixed-squares {[0 0] 1}]
        (should=
          {:mines #{} :vacancies #{}}
          (for-fixed-squares fixed-squares grid))))

  )

(ns meinsweeper.spec-helper
  (:require
    [meinsweeper.ai.facts :refer :all]
    [clojure.core.logic :as lg]))

(defmacro remove-facts [relation n-args]
  (let [argvector (vec (repeatedly n-args gensym))]
    `(let [facts# (lg/run* ~argvector
                           (~relation ~@argvector))]
       (lg/retractions ~relation (vec facts#)))))

(defn remove-all-facts []
  (remove-facts numbered-square 2)
  (remove-facts mine-square 2))

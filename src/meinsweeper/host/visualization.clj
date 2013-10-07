(ns meinsweeper.host.visualization
  (:require
    [meinsweeper.square-names :refer :all]
    [clojure.walk :as walk]
    [meinsweeper.host :refer :all]))

(defn symbol-for [square-contents]
  (cond
    (= square-contents mine)  "*"
    (= square-contents unknown) "☐"
    (number? square-contents) (if (pos? square-contents)
                                (str square-contents)
                                " ")))

(defn viewable-game [game-state]
  (walk/postwalk
    #(if (seq? %) (vec %) %)
    (for [row game-state]
      (for [square row]
        (symbol-for square)))))

(defn print-viewable-game [game-structure]
  (println (clojure.string/join "\n" (map #(clojure.string/join "|" %)  (viewable-game game-structure)))))


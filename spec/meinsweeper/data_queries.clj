(ns meinsweeper.data-queries)

(defn all-equal [potentially-equal]
  (= 1 (count (distinct potentially-equal))))

(defn indices-of-equality [maps]
  (filter (fn [key]
            (let [elements-at-index (map #(get % key) maps)]
              (all-equal elements-at-index)))
          (keys (first maps))))


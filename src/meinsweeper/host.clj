(ns meinsweeper.host
  (:require
    [clojure.core.logic :as lg]
    [meinsweeper.square-names :refer :all]
    [meinsweeper.grid :refer :all]))

(lg/defrel underlying-vacancy coordinates _)
(lg/defrel underlying-mine coordinates _)
(lg/defrel neighborless-vacancy coordinates _)

(declare neighbor-mine-count)

(defn random-mine-positions [count rows cols]
  (let [all-positions (all-positions rows cols)
        shuffled (shuffle all-positions)]
    (set (take count shuffled))))

(defn all-mines []
  (set (lg/run* [s]
                (underlying-mine s nil)
                ))
  )

(defn mine? [square]
  (contains? (all-mines) square)
  ;(let [found-mine (lg/run* [mine-square]
  ;                      (lg/== mine-square square)
  ;                      (underlying-mine mine-square nil))]
  ;  ;(prn "Square: " square " had " found-mine " at it")
  ;  (not (empty? found-mine)))
  )

(defn build-game-facts [mines rows cols]
  (let [all-positions (set (all-positions rows cols))
        mine-positions (random-mine-positions mines rows cols)
        vacant-positions (clojure.set/difference all-positions mine-positions)]
  (doseq [mine-square mine-positions]
    (lg/fact underlying-mine mine-square nil))
  (doseq [vacant-square vacant-positions] (lg/fact underlying-vacancy vacant-square nil))))

(defn print-viewable-game [game-structure]
  (println (clojure.string/join "\n" (map #(clojure.string/join "|" %)  game-structure))))

(defn symbol-for [square exposed-squares]
  (if (contains? exposed-squares square)
    (cond
      (mine? square)
      "*"
      :else
      (if (pos? (neighbor-mine-count square))
        (str (neighbor-mine-count square))
        " "))
    "☐"))

(defn viewable-game [exposed-squares max-row max-col]
  (for [row (range (inc max-row))]
    (for [col (range (inc max-col))]
      (symbol-for [row col] exposed-squares))))

(defn neighbor-mine-count [square]
  (count
    (lg/run* [r c]
             (adjacent [r c] square)
             (underlying-mine [r c] nil))))

(defn neighbored-vacancies []
  (set (lg/run* [r c]
                (lg/fresh [mine-row mine-col]
                  (adjacent [r c] [mine-row mine-col])
                  (underlying-vacancy [r c] nil)
                  (underlying-mine [mine-row mine-col] nil)))))

(defn neighborless-vacancies []
  (let [all-vacancies (set (lg/run* [square] (underlying-vacancy square nil)))]
    (clojure.set/difference all-vacancies (neighbored-vacancies))))

(defn record-neighborless-vacancies []
  (doseq [square (neighborless-vacancies)]
    (lg/fact neighborless-vacancy square nil)))

(def connected?
  (lg/tabled
    [target current-candidate]
    (underlying-vacancy current-candidate nil)
    (lg/conde
      [(adjacent target current-candidate)]
      [(lg/fresh
         [intermediate-row intermediate-col]
         (underlying-vacancy [intermediate-row intermediate-col] nil)
         (adjacent [intermediate-row intermediate-col] current-candidate)
         (neighborless-vacancy [intermediate-row intermediate-col] nil)
         (connected? target [intermediate-row intermediate-col]))])))

(defn expand-for-click [click-location]
  (let [[place state] click-location]
    (record-neighborless-vacancies)
    (if (= state mine)
      #{place}
      (set (lg/run* [row col]
                    (connected? place [row col]))))))

(defn crap-for [square]
  (if (mine? square)
    mine
    (neighbor-mine-count square)))

(defn state-map [visible-stuff rows cols]
  (vec (for [row (range rows)]
    (vec (for [col (range cols)]
     (if (contains? visible-stuff [row col])
      (crap-for [row col])
      unknown))))))

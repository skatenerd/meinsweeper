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

(defn mine? [square]
  (let [found-mine (lg/run* [mine-square]
                        (lg/== mine-square square)
                        (underlying-mine mine-square nil))]
    (not (empty? found-mine)))
  )

(defn build-game-facts [mines rows cols]
  (let [all-positions (set (all-positions rows cols))
        mine-positions (random-mine-positions mines rows cols)
        vacant-positions (clojure.set/difference all-positions mine-positions)]
  (doseq [mine-square mine-positions]
    (lg/fact underlying-mine mine-square nil))
  (doseq [vacant-square vacant-positions] (lg/fact underlying-vacancy vacant-square nil))))

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


(defn- connected-squares [square]
  (let [connected-squares (set (lg/run* [row col]
                                        (connected? square [row col])))]
   (conj
    connected-squares
    square)))

(defn individual-vacant-click [grid click-location]
  (case (square-at grid click-location)
    vacant #{click-location}
    mine #{kaboom}
    )
  )

(defn expand-for-click [click-location grid]
  (record-neighborless-vacancies)
  (if (= unknown (square-at grid click-location))
    (connected-squares click-location)
    #{click-location}))

(def memoized-expand-for-click (memoize expand-for-click))

(defn square-state [square]
  (if (mine? square)
    kaboom
    (neighbor-mine-count square)))

(defn mark-squares [grid to-put squares]
  (reduce
    (fn [updated-grid current-square]
      (update-in updated-grid current-square (fn [_] (to-put current-square))))
    grid
    squares))

(defn clear-vacancies [grid vacancies]
  (reduce
    (fn [updated-grid current-vacancy]
      (let [for-vacancy (expand-for-click current-vacancy updated-grid)]
        (mark-squares updated-grid square-state for-vacancy)))
    grid
    vacancies))

(defn mark-mines [grid squares]
  (mark-squares grid (fn [_] mine) squares))

(defn for-theory [theory rows cols]
  (let [after-vacancies-cleared (clear-vacancies (empty-grid rows cols) (:vacancies theory))]
    (mark-mines after-vacancies-cleared (:mines theory))
    ))

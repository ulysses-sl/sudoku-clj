(ns sudoku-clj.loader
  (:require [clojure.string :as str]))

;;
;; Board loading and sanity checking routines
;;

(defn load-board-file
  [board-file]
  (slurp board-file))

(defn parse-row-from-string
  "Reconstructs a single row of sudoku board from the string"
  [row-string]
  (->> (str/split row-string #" ")
       (map read-string)
       (vec)))

(defn parse-board-from-string
  "Reconstructs a sudoku board from the string"
  [board-string]
  (->> board-string
       (str/split-lines)
       (map parse-row-from-string)
       (vec)))

(defn rows
  "Returns the rows of the board"
  [board]
  board)

(defn columns
  "Returns the columns of the board"
  [board]
  (apply map vector board))

(defn group-by-3
  "Groups the given collection by 3"
  [coll]
  (partition 3 coll))

(defn three-threes-from-3-rows
  "Gets 3x3s from given 3 rows"
  [rows]
  (->> rows
       (map group-by-3)
       (columns)
       (map #(apply concat %))))

(defn three-threes
  "Returns the 3x3s of the board"
  [board]
  (->> board
       (group-by-3)
       (mapcat three-threes-from-3-rows)))

(defn has-9
  "Checks if the collection has 9 items"
  [coll]
  (= 9 (count coll)))

(defn is-square-valid
  "Checks if the given square x is an integer from 0 to 9"
  [x]
  (and (integer? x) (<= 0 x 9)))

(defn is-everything-unique
  "Checks if every item in the collection is unique"
  [coll]
  (= coll (distinct coll)))

(defn get-non-zeros
  "Gets non-zero items from the collection"
  [coll]
  (filter #(not= 0 %) coll))

(defn is-line-valid
  "Checks if a line has 9 numbers and all the non-zero nums are unique"
  [line]
  (and (every? is-square-valid line)
       (->> line
            (get-non-zeros)
            (is-everything-unique))))

(defn check-all-lines
  "Checks every line to see if the condition meets"
  [line-condition board]
  (every? line-condition board))

(defn check-board-position
  "Returns closure for checking the corresponding board positions for condition"
  [condition position]
  (fn [board]
    (->> (position board)
         (check-all-lines condition))))

(defn check-board
  "Checks the entire board for the condition"
  [condition board]
  (and ((check-board-position condition rows) board)
       ((check-board-position condition columns) board)
       ((check-board-position condition three-threes) board)))

(defn is-board-valid
  [board]
  (check-board is-line-valid board))

;;
;; Board preprocess
;;

(def set-of-possible-nums #{1 2 3 4 5 6 7 8 9})

(defn make-guess-set-from-number
  "Return guess sets if 0 or singleton set of itself otherwise"
  [num]
  (if (= 0 num)
    set-of-possible-nums
    #{num}))

(defn make-row-of-sets
  [row]
  (->> row
       (map make-guess-set-from-number)
       (vec)))

(defn make-board-of-sets
  [board]
  (->> board
       (map make-row-of-sets)
       (vec)))

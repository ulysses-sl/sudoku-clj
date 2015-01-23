(ns sudoku-clj.core
  (:gen-class)
  (:require [clojure.string :as str]))

;;
;; Board loading and sanity checking routines
;;

(def board-file "board.txt")

(defn load-board-file
  []
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



(defn all-true
  "Checks if everything in the collection is true"
  [coll]
  (reduce #(and %1 %2) true coll))

(defn is-line-valid
  "Checks if a line has 9 numbers and all the non-zero nums are unique"
  [line]
  (and (->> line
            (map is-square-valid)
            (all-true))
       (->> line
            (get-non-zeros)
            (is-everything-unique))))

(defn check-all-lines
  "Checks every line to see if the condition meets"
  [condition board]
  (->> board
       (map condition)
       (all-true)))

(defn check-board-position
  "Returns closure for checking the corresponding board positions for condition"
  [condition position]
  (fn [board]
    (->> board
         (position)
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

;;
;; Check board consistency
;;

(defn is-line-solved
  "Checks if line has permutation of (1..9)"
  [line]
  (= #{1 2 3 4 5 6 7 8 9}
     (->> line (apply concat) (set))))

(defn is-board-solved
  "Checks if all combos in the board is solved"
  [board]
  (check-board is-line-solved board))

;; All the board positions
(def board-positions
  (let [coords (take 9 (range))]
    (for [row coords
          column coords]
      [row column])))

;; Get the value at the specific coordinate of board
(def get-square get-in)

;; Set a square in the board to given value
(def set-square assoc-in)

(defn get-member-row
  "Get all coords on the same row"
  [[row x]]
  (for [column (take 9 (range))]
    [row column]))

(defn get-member-column
  "Get all coords on the same column"
  [[x column]]
  (for [row (take 9 (range))]
    [row column]))

(defn round-down-by
  "Round down first number to second number's multiple"
  [num div]
  (- num (mod num div)))

(defn get-upper-left-corner-in-three-three
  "Get the coord of ULC in 3x3"
  [[row column]]
  (let [ulc-row (round-down-by row 3)
        ulc-column (round-down-by column 3)]
      [ulc-row ulc-column]))

(defn get-member-three-three
  "Get all coords on the same 3x3"
  [coord]
  (let [[ulc-row ulc-column]
        (get-upper-left-corner-in-three-three coord)]
    (for [new-row (range ulc-row (+ ulc-row 3))
          new-column (range ulc-column (+ ulc-column 3))]
      [new-row new-column])))

(defn get-related-squares
  "Get all related coords on row, column, 3x3 without itself"
  [coord]
  (let [adjacents
        (-> (concat
              (get-member-row coord)
              (get-member-column coord)
              (get-member-three-three coord))
            (distinct))]
    (remove #(= coord %) adjacents)))

;;(some #(= 0 (count %)) coll)

(defn mrv
  "minimum remaining values"
  []
  false)

(defn lcv
  "least constraining values"
  []
  false)

(defn backtrack
  []
  '[])



(defn -main
  "Receive a filename through args and solve the content"
  [& args]
  (println "Hello, World!"))

(ns sudoku-clj.core-spec
  (:require [speclj.core :refer :all]
            [sudoku-clj.core :refer :all]))

(describe "parse-row-from-string"
  (it "converts  \"1 2 3\" to [1 2 3]"
    (should= '[1 2 3] (parse-row-from-string "1 2 3"))))

(describe "parse-board-from-string"
  (it "converts \"1 2 3  to [[1 2 3]
                                    4 5 6\"     [4 5 6]]"
    (should= '[[1 2 3] [4 5 6]] (parse-board-from-string "1 2 3\n4 5 6"))))

(describe "rows"
  (it "converts [[1 2 3] [4 5 6] [7 8 9]] to [[1 2 3] [4 5 6] [7 8 9]]"
    (should= '[[1 2 3] [4 5 6] [7 8 9]] (rows '[[1 2 3] [4 5 6] [7 8 9]]))))

(describe "columns"
  (it "converts [[1 2 3] [4 5 6] [7 8 9]] to [[1 4 7] [2 5 8] [3 6 9]]"
    (should= '[[1 4 7] [2 5 8] [3 6 9]] (columns '[[1 2 3] [4 5 6] [7 8 9]]))))

(describe "group-by-3"
  (it "groups [1 2 3 4 5 6] into [[1 2 3] [4 5 6]]"
    (should= '[[1 2 3] [4 5 6]] (group-by-3 '[1 2 3 4 5 6]))))

(describe "three-threes-from-3-rows"
  (it "converts [[1 2 3 1 2 3]  to [[1 2 3 4 5 6 7 8 9]
                                      [4 5 6 4 5 6]      [1 2 3 4 5 6 7 8 9]]
                                      [7 8 9 7 8 9]]"
    (should= '[[1 2 3 4 5 6 7 8 9] [1 2 3 4 5 6 7 8 9]]
             (three-threes-from-3-rows '[[1 2 3 1 2 3] [4 5 6 4 5 6] [7 8 9 7 8 9]]))))

(describe "three-threes"
  (it "converts [[1 2 3 1 2 3]  to [[1 2 3 4 5 6 7 8 9]
                          [4 5 6 4 5 6]      [1 2 3 4 5 6 7 8 9]
                          [7 8 9 7 8 9]      [1 2 3 4 5 6 7 8 9]
                          [1 2 3 1 2 3]      [1 2 3 4 5 6 7 8 9]]
                          [4 5 6 4 5 6]
                          [7 8 9 7 8 9]]"
    (should= '[[1 2 3 4 5 6 7 8 9] [1 2 3 4 5 6 7 8 9] [1 2 3 4 5 6 7 8 9] [1 2 3 4 5 6 7 8 9]]
             (three-threes '[[1 2 3 1 2 3] [4 5 6 4 5 6] [7 8 9 7 8 9] [1 2 3 1 2 3] [4 5 6 4 5 6] [7 8 9 7 8 9]]))))




(describe "has-9"
  (it "returns true on 9-element vector [1 2 3 4 5 6 7 8 9]"
    (should (has-9 [1 2 3 4 5 6 7 8 9])))

  (it "returns false on 7-element vector [1 2 3 4 5 6 7]"
    (should-not (has-9 [1 2 3 4 5 6 7]))))

(describe "is-square-valid"
  (it "returns true on a valid one-digit non-negative integer"
    (should (is-square-valid 1))))

(describe "is-everything-unique"
  (it "returns true on vector with unique elements"
    (should (is-everything-unique '[1 2 3])))

  (it "returns false on vectors with duplicate elements"
    (should-not (is-everything-unique '[1 2 3 1]))))

(describe "get-non-zeros"
  (it "returns only the non-zero elements of a collection"
    (should= '[1 2 3 4 5] (get-non-zeros '[1 0 2 3 0 0 4 5 0]))))



(describe "all-true"
  (it "returns true when everything in the collection is true"
    (should (all-true '(true true true))))

  (it "returns false when anything in the collection is not true"
    (should-not (all-true '(true false true)))))

(describe "is-line-valid"
  (it "returns true on a vector with 9 valid unique squares"
    (should (is-line-valid '[1 2 3 4 5 0 7 8 9]))))

(describe "check-all-lines"
  (it "returns true when all the lines pass the check function"
    (should (check-all-lines all-true [[true true] [true true]]))))

(describe "check-board-position"
  (it "returns true when all the rows pass the check function"
    (should ((check-board-position is-line-valid rows) [[1 2 3 4 5 6 7 8 9]])))

  (it "returns true when all the columns pass the check function"
    (should ((check-board-position is-line-valid columns) [[1] [2] [3] [4] [5] [6] [7] [8] [9]])))

  (it "returns true when all the 3x3s pass the check function"
    (should ((check-board-position is-line-valid three-threes) [[1 2 3] [4 5 6] [7 8 9]]))))

(describe "check-board"
  (it "returns true when the entire board passes the check function"
    (should (check-board all-true [[true true true] [true true true] [true true true]]))))



(describe "make-guess-set-from-number"
  (it "returns a set of integer 1 through 9 when 0 is received"
    (should= #{1} (make-guess-set-from-number 1)))

  (it "returns a set of integer 1 through 9 when 0 is received"
    (should= #{1 2 3 4 5 6 7 8 9} (make-guess-set-from-number 0))))

(describe "make-row-of-sets"
  (it "receives a collection of numbers and returns row of sets"
    (should= '[#{1} #{2} #{1 2 3 4 5 6 7 8 9} #{3}] (make-row-of-sets '[1 2 0 3]))))

(describe "make-board-of-sets"
  (it "receives a board of integers and returns a board of sets"
    (should= '[[#{1} #{2}] [#{3} #{4}]] (make-board-of-sets [[1 2] [3 4]]))))



(describe "is-line-solved"
  (it "returns true when a collection of 9 unique singleton sets are given"
    (should (is-line-solved [#{1} #{2} #{3} #{4} #{5} #{6} #{7} #{8} #{9}]))))


(def row-3-5 '[[3 0] [3 1] [3 2] [3 3] [3 4] [3 5] [3 6] [3 7] [3 8]])
(def column-3-5 '[[0 5] [1 5] [2 5] [3 5] [4 5] [5 5] [6 5] [7 5] [8 5]])
(def three-three-3-5 '[[3 3] [3 4] [3 5] [4 3] [4 4] [4 5] [5 3] [5 4] [5 5]])
(def combined-3-5 (->> (concat row-3-5 column-3-5 three-three-3-5) (distinct) (remove #(= '[3 5] %))))

(describe "get-member-row"
  (it "returns all coords on the same row"
    (should= row-3-5 (get-member-row [3 5]))))

(describe "get-member-column"
  (it "returns all coords on the same column"
    (should= column-3-5 (get-member-column [3 5]))))

(describe "round-down-by"
  (it "returns first number rounded down by the second number"
    (should= 9 (round-down-by 11 3))))

(describe "get-upper-left-corner-in-three-three"
  (it "returns the coord of upper left corner square of current 3x3"
    (should= '[3 3] (get-upper-left-corner-in-three-three [3 5]))))

(describe "get-member-three-three"
  (it "returns all coords on the same 3x3"
    (should= three-three-3-5 (get-member-three-three [3 5]))))

(describe "get-related-squares"
  (it "returns all related coords on row, column, 3x3 without itself"
    (should= combined-3-5 (get-related-squares [3 5]))))






(run-specs)

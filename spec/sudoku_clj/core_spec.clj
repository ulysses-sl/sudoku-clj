(ns sudoku-clj.core-spec
  (:require [speclj.core :refer :all]
            [sudoku-clj.core :refer :all]))

(describe "make-row-from-string"
  (it "converts  \"1 2 3\" to [1 2 3]"
    (should= '[1 2 3] (make-row-from-string "1 2 3"))))

(describe "make-board-from-string"
  (it "converts \"1 2 3  to [[1 2 3]\n                                      4 5 6\"     [4 5 6]]"
    (should= '[[1 2 3] [4 5 6]] (make-board-from-string "1 2 3\n4 5 6"))))

(describe "rows"
  (it "converts [[1 2 3] [4 5 6] [7 8 9]] to [[1 2 3] [4 5 6] [7 8 9]]"
    (should= '[[1 2 3] [4 5 6] [7 8 9]] (rows '[[1 2 3] [4 5 6] [7 8 9]]))))

(describe "columns"
  (it "converts [[1 2 3] [4 5 6] [7 8 9]] to [[1 4 7] [2 5 8] [3 6 9]]"
    (should= '[[1 4 7] [2 5 8] [3 6 9]] (columns '[[1 2 3] [4 5 6] [7 8 9]]))))

(describe "three-threes"
  (it "converts [[1 2 3 1 2 3] [4 5 6 4 5 6] [7 8 9 7 8 9]] to [[1 2 3 4 5 6 7 8 9] [1 2 3 4 5 6 7 8 9]]"
    (should= '[[1 2 3 4 5 6 7 8 9] [1 2 3 4 5 6 7 8 9]] (three-threes '[[1 2 3 1 2 3] [4 5 6 4 5 6] [7 8 9 7 8 9]]))))

(describe "has-9"
  (it "returns true on 9-element vector [1 2 3 4 5 6 7 8 9]"
    (should (has-9 [1 2 3 4 5 6 7 8 9])))

  (it "returns false on 7-element vector [1 2 3 4 5 6 7]"
    (should-not (has-9 [1 2 3 4 5 6 7]))))

(describe "is-valid-square"
  (it "returns true on a valid one-digit non-negative integer"
    (should (is-valid-square 1))))

(describe "is-everything-unique"
  (it "returns true on vector with unique elements"
    (should (is-everything-unique '[1 2 3])))

  (it "returns false on vectors with duplicate elements"
    (should-not (is-everything-unique '[1 2 3 1]))))

(describe "is-valid-line"
  (it "returns true on a vector with 9 valid unique squares"
    (should (is-valid-line '[1 2 3 4 5 6 7 8 9]))))

(describe "are-rows-valid"
  (it "returns true on [[1 2 3 4 5 6 7 8 9]]"
    (should (are-rows-valid [[1 2 3 4 5 6 7 8 9]]))))

(describe "are-columns-valid"
  (it "returns true on [[1] [2] [3] [4] [5] [6] [7] [8] [9]]"
    (should (are-columns-valid [[1] [2] [3] [4] [5] [6] [7] [8] [9]]))))

(describe "are-three-threes-valid"
  (it "returns true on [[1 2 3] [4 5 6] [7 8 9]]"
    (should (are-three-threes-valid [[1 2 3] [4 5 6] [7 8 9]]))))

(run-specs)

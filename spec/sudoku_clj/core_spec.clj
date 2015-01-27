(ns sudoku-clj.core-spec
  (:require [speclj.core :refer :all]
            [sudoku-clj.loader :refer :all]
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




(describe "is-line-valid"
  (it "returns true on a vector with 9 valid unique squares"
    (should (is-line-valid '[1 2 3 4 5 0 7 8 9]))))

(describe "check-all-lines"
  (it "returns true when all the lines pass the check function"
    (should (check-all-lines #(every? pos? %) [[4 1] [2 3]]))))

(describe "check-board-position"
  (it "returns true when all the rows pass the check function"
    (should ((check-board-position is-line-valid rows) [[1 2 3 4 5 6 7 8 9]])))

  (it "returns true when all the columns pass the check function"
    (should ((check-board-position is-line-valid columns) [[1] [2] [3] [4] [5] [6] [7] [8] [9]])))

  (it "returns true when all the 3x3s pass the check function"
    (should ((check-board-position is-line-valid three-threes) [[1 2 3] [4 5 6] [7 8 9]]))))

(describe "check-board"
  (it "returns true when the entire board passes the check function"
    (should (check-board #(every? pos? %) [[1 2 3] [1 2 3] [1 2 3]]))))



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

(describe "get-relatives"
  (it "returns all related coords on row, column, 3x3 without itself"
    (should= combined-3-5 (get-relatives [3 5]))))

(describe "update-table"
  (it "returns updated hash table with relatives mapped to coord"
    (should= {[0 0] (get-relatives [0 0])} (update-table {} [[0 0]]))))

(describe "get-related-squares"
  (it "returns all cached related coords on row, column, 3x3 without itself"
    (should= combined-3-5 (get-related-squares [3 5]))))

(describe "remove-unless-fail"
  (it "removes item from the set at coord and return board if the set is not empty"
    (should= '[[#{1 2} #{3 4}] [#{5 6} #{7}]] (remove-unless-fail '[[#{1 2} #{3 4}] [#{5 6} #{7 8}]] '[1 1] 8)))

  (it "return nil if the set would become empty when you remove the item"
    (should= nil (remove-unless-fail '[[#{1 2} #{3 4}] [#{5 6} #{8}]] '[1 1] 8))))

(def testboard-1
  '[[#{} #{} #{} #{} #{} #{} #{} #{} #{}]
    [#{} #{} #{} #{} #{} #{} #{} #{} #{}]
    [#{} #{} #{} #{} #{} #{} #{} #{} #{}]
    [#{} #{} #{} #{} #{} #{} #{} #{} #{}]
    [#{} #{} #{} #{} #{} #{} #{} #{} #{}]
    [#{} #{} #{} #{} #{} #{} #{} #{} #{}]
    [#{} #{} #{} #{} #{} #{} #{} #{} #{}]
    [#{} #{} #{} #{} #{} #{} #{} #{} #{}]
    [#{} #{} #{} #{} #{} #{} #{} #{} #{}]])

(def testboard-2
  '[[#{} #{2} #{3} #{1 2} #{2} #{3} #{1} #{2} #{3}]
    [#{2} #{3} #{1 2 3} #{} #{} #{} #{} #{} #{}]
    [#{3} #{1 2 3} #{2} #{} #{} #{} #{} #{} #{}]
    [#{1 2} #{} #{} #{} #{} #{} #{} #{} #{}]
    [#{2} #{} #{} #{} #{} #{} #{} #{} #{}]
    [#{3} #{} #{} #{} #{} #{} #{} #{} #{}]
    [#{1} #{} #{} #{} #{} #{} #{} #{} #{}]
    [#{2} #{} #{} #{} #{} #{} #{} #{} #{}]
    [#{3} #{} #{} #{} #{} #{} #{} #{} #{}]])



(describe "get-item-sharing-neighbors"
  (it "returns the list of coords of item sharing neighbors"
    (should= #{[0 3] [0 6] [1 2] [2 1] [3 0] [6 0]} (set (get-item-sharing-neighbors testboard-2 [0 0] 1)))))

(describe "get-influence-on-neighbors"
  (it "measures the constraints the number enforces on adjacent boards"
    (should= 6 (get-influence-on-neighbors testboard-2 [0 0] 1))))

(describe "get-constrained-neighbors"
  (it "measures the constraints the number enforces on adjacent boards"
    (should= #{[0 3] [0 6] [3 0] [6 0]} (set (get-constrained-neighbors testboard-2 [0 0] 1)))))

(run-specs)

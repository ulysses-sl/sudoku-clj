(ns sudoku-clj.core-spec
  (:require [speclj.core :refer :all]
            [sudoku-clj.core :refer :all]))

(describe "Sudoku Solver Components"
  (before-all
    (println "Start testing"))

  (after-all
    (println "Finished testing"))

  ;(before
  ;  (println "Before each test"))

  ;(after
  ;  (println "After each test"))

  (it "String \"1 2 3\" converts to row [1 2 3]"
    (println "Test 1. make-row-from-string")
    (should= '[1 2 3] (make-row-from-string "1 2 3")))

  (it "String \"1 2 3  converts to board [[1 2 3]\n                                    4 5 6\"                    [4 5 6]]"
    (println "Test 2. make-board-from-string")
    (should= '[[1 2 3] [4 5 6]] (make-board-from-string "1 2 3\n4 5 6")))
)

(run-specs)

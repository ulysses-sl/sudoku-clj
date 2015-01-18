(ns sudoku-clj.core
  (:gen-class))

(def board-file "board.txt")

(defn load-board-file
  []
  (slurp board-file))

(defn make-row-from-string
  "Reconstructs a single row of sudoku board from the string"
  [row-string]
  ":-(")

(defn make-board-from-string
  "Reconstructs a sudoku board from the string"
  [board-string]
  ":-(")

(defn -main
  "Receive a filename through args and solve the content"
  [& args]
  (println "Hello, World!"))

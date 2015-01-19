(ns sudoku-clj.core
  (:gen-class)
  (:require [clojure.string :as str]))

(def board-file "board.txt")

(defn load-board-file
  []
  (slurp board-file))

(defn make-row-from-string
  "Reconstructs a single row of sudoku board from the string"
  [row-string]
  (->> (str/split row-string #" ") (map read-string) (apply vector)))

(defn make-board-from-string
  "Reconstructs a sudoku board from the string"
  [board-string]
  (->> board-string (str/split-lines) (map make-row-from-string) (apply vector)))

(defn rows
  [board]
  false)

(defn columns
  [board]
  false)

(defn three-threes
  [board]
  false)

(defn has-9
  [coll]
  false)

(defn is-valid-square
  [x]
  false)

(defn is-everything-unique
  [x]
  false)

(defn is-valid-line
  [line]
  false)

(defn are-rows-valid
  [board]
  false)

(defn are-columns-valid
  [board]
  false)

(defn are-three-threes-valid
  [board]
  false)

(defn is-board-valid
  [board]
  false)

(defn -main
  "Receive a filename through args and solve the content"
  [& args]
  (println "Hello, World!"))

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
  "poop")

(defn columns
  [board]
  "poop")

(defn three-threes
  [board]
  "poop")

(defn has-9
  [coll]
  "poop")

(defn is-valid-square
  [x]
  "poop")

(defn is-everything-unique
  [x]
  "poop")

(defn is-valid-line
  [line]
  "poop")

(defn are-rows-valid
  [board]
  "poop")

(defn are-columns-valid
  [board]
  "poop")

(defn are-three-threes-valid
  [board]
  "poop")

(defn -main
  "Receive a filename through args and solve the content"
  [& args]
  (println "Hello, World!"))

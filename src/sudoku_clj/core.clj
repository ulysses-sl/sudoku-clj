(ns sudoku-clj.core
  (:gen-class)
  (:require [clojure.string :as str]
            [sudoku-clj.loader :refer :all]))

;;
;; Check board consistency
;;

(defn is-line-solved
  "Checks if line has permutation of (1..9)"
  [line]
  (= '[1 2 3 4 5 6 7 8 9]
     (->> line (apply concat) (sort))))

(defn is-board-solved
  "Checks if all combos in the board is solved"
  [board]
  (if board
    (check-board is-line-solved board)
    true))

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

(defn get-relatives
  [coord]
  (->> `(~(get-member-row coord)
         ~(get-member-column coord)
         ~(get-member-three-three coord))
       (apply concat)
       (distinct)
       (remove #(= coord %))))

(defn update-table
  [table coords]
  (if (empty? coords)
    table
    (let [coord (first coords)
          next-coords (rest coords)
          relatives (get-relatives coord)]
      (update-table (assoc table coord relatives) next-coords))))

(def related-squares
  (update-table {} board-positions))

(defn get-related-squares
  "Get all related coords on row, column, 3x3 without itself"
  [coord]
  (get related-squares coord))

(defn get-item-sharing-neighbors
  "list of neighbors"
  [board coord item]
  (->> (get-related-squares coord)
       (filter #(contains? (get-in board %) item))))

(defn get-influence-on-neighbors
  "how many neighbors share this number"
  [board coord item]
  (->> (get-item-sharing-neighbors board coord item)
       (count)))

(defn get-constrained-neighbors
  "how many neighbors are zero-holed (<= 1 after propagation)"
  [board coord item]
  (->> (get-item-sharing-neighbors board coord item)
       (filter #(->> (get-in board %) (count) (>= 2)))))

(defn remove-unless-fail
  "remove item from the set at coord and return board unless it becomes empty"
  [board coord item]
  (let [new-square (disj (get-square board coord) item)]
    (if (seq new-square)
      (set-square board coord new-square)
      nil)))

(defn remove-everything-unless-fail
  "remove item from all listed coords unless any becomes empty"
  [board coord-list item]
  (if (empty? coord-list)
    board
    (let [coord (first coord-list)
          next-coords (rest coord-list)
          new-board (remove-unless-fail board coord item)]
      (if (not-empty new-board)
        (remove-everything-unless-fail new-board next-coords item)
        nil))))

(def queue (clojure.lang.PersistentQueue/EMPTY))

(defn add-to-queue
  [original-queue additions]
  (if (empty? additions)
    original-queue
    (add-to-queue (conj original-queue (first additions)) (rest additions))))

(defn stringify-line
  [line]
  (str/join " " (map #(apply str %) line)))

(defn stringify-board
  [board]
  (if board
    (str/join "\n" (map stringify-line board))))

(defn propagate-consistency
  "improve board until no longer can be improved"
  [board coords-to-check]
  (if (empty? coords-to-check)
    board
    (let [coord (first coords-to-check)
          next-coords (pop coords-to-check)
          num (first (get-square board coord))
          neighbors (get-item-sharing-neighbors board coord num)
          const-neighbors (get-constrained-neighbors board coord num)
          new-board (remove-everything-unless-fail board neighbors num)]
      (if new-board
        (propagate-consistency new-board (add-to-queue next-coords const-neighbors))
        nil))))

(defn enforce-consistency
  "Returns enforced board or nil if impossible"
  [board]
  (->> board-positions
       (filter #(->> (get-square board %) (count) (== 1)))
       (add-to-queue queue)
       (propagate-consistency board)))

(defn mrv
  "returns coords of (n > 1) squares sorted by minimum remaining values"
  [board]
  (->> board-positions
       (filter #(->> (get-square board %) (count) (< 1)))
       (sort-by #(->> (get-square board %) (count)))))

(defn lcv
  "returns values of the square sorted by least constraining values"
  [board coord]
  (->> (get-square board coord)
       (sort-by #(get-influence-on-neighbors board coord %))))

(defn get-child-boards
  [board]
  (->> (for [coord (mrv board)
             val (lcv board coord)
             updated-board (-> (set-square board coord #{val})
                               (propagate-consistency (conj queue coord)))]
         updated-board)
       (partition 9)
       (map vec)))

(def visited (atom #{}))

(defn backtrack
  [board]
  ;(print-board board)
  (cond
    (contains? @visited (hash board)) nil
    (is-board-solved board) board
    :else (do
            (swap! visited (fn [x] (conj x (hash board))))
            (->> (get-child-boards board)
                 (map #(backtrack %))
                 (keep identity)
                 (first)))))

(defn solve-board
  [filename]
  (let [board (-> filename (load-board-file) (parse-board-from-string))]
    (str filename
         "\n"
         (if (is-board-valid board)
           (let [initial-board (make-board-of-sets board)
                 finished-board (backtrack (enforce-consistency initial-board))]
             (if finished-board
               (stringify-board finished-board)
               "Unsolvable!"))
           "Invalid board!")
         "\n")))

(defn -main
  "Receive a filename through args and solve the content"
  [& args]
  (if (not-empty args)
    (doseq [board (doall (pmap solve-board args))]
      (println board))
    (println "Syntax: sudoku-clj [filename1 ...]"))
  (shutdown-agents))
  ;(System/exit 0))

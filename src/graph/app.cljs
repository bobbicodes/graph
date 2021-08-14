(ns graph.app
  (:require [reagent.core :as r]))

(defn svg-node [label x y]
  [:g [:circle {:fill "yellow" :stroke "black" :cx x :cy y :r 18}]
   [:text {:text-anchor "middle" :x x :y (+ 4 y) :font-family "Monospace"
           :font-size 14} label]])

(defn svg-leaf [label x y]
  [:g [:rect {:fill "yellow" :stroke "black" :x (- x 14) :y (- y 14) :width 28 :height 28}]
   [:text {:text-anchor "middle" :x x :y (+ 4 y) :font-family "Monospace"
           :font-size 14} label]])

(defn undirected-edge
  "Coordinates are for the text label (edge weight).
   Path is the edge spline.
   Polygon points are for the arrows."
  [path]
  [:g
   [:path {:fill "none" :stroke "black" :d path}]])


(defn svg-edge
  "Coordinates are for the text label (edge weight).
   Path is the edge spline.
   Polygon points are for the arrows."
  [label x y path points]
  [:g
   [:path {:fill "none" :stroke "black" :d path}]
   [:polygon {:fill "black" :stroke "black" :points points}]
   [:text {:text-anchor "middle" :x x :y y :font-family "Monospace" :font-size 14.00} label]])

(defn g []
  {:1 {:2 1 :3 2}
   :2 {:4 3 :5 4}
   :3 {:6 5 :7 6}
   :4 {}
   :5 {}
   :6 {}
   :7 {}})

(def g3
  {:1 {:2 1 :3 2}
   :2 {:4 3 :5 4}
   :3 {:6 5 :7 6}
   :4 {:8 1 :9 1}
   :5 {:10 1 :11 1}
   :6 {:12 1 :13 1}
   :7 {:14 1 :15 1}
   :8 {}
   :9 {}
   :10 {}
   :11 {}
   :12 {}
   :13 {}
   :14 {}
   :15 {}})

#_(defn parent [node tree]
    (first (filter #(contains? (get tree %) node) (keys tree))))

;(parent :2 g3)

#_(defn node-depth [node tree]
    (loop [n node parents []]
      (if (nil? (parent n tree))
        (count parents)
        (recur (parent n tree) (conj parents (parent n tree))))))

;(node-depth :8 g3)

#_(defn tree-height [tree]
    (inc (apply max (map #(node-depth % tree) (keys tree)))))

;(tree-height g3)

(defn leaf-nodes [tree]
  (filter #(empty? (get tree %)) (keys tree)))

(count (leaf-nodes g3))

(.-innerWidth js/window)
(.-innerHeight js/window)

(filter (fn [[node children]] (seq children)) g3)

(defonce my-nodes (r/atom [1]))

(reset! my-nodes (vec (range 1 16)))

(- 27 99)
(- 99 171)
(- 171 243)
(/ (- 27 531) 8)

; on what level is the node?
; bottom level is -18, each one is 87 up from there:

(defn tree-height
  "Returns the number of levels high a binary heap
   consisting of n elements must be, or what level
   node n is on."
  [n]
  (inc (.floor js/Math (.log2 js/Math n))))

(defn tree-width
  "Returns the number of nodes wide a binary heap
   consisting of n elements must be."
  [n]
  (.pow js/Math 2 (dec (tree-height n))))

(defn to-the-right
  "Returns how many positions to the right a node must be
   in a binary tree or heap."
  [n]
  (inc (mod n (tree-width n))))

(defn node-x
  "Returns the x-coordinate of node n."
  [n]
  (let [row-width (tree-width n)
        viewbox-width 566]
    (+ (* (/ viewbox-width row-width) (dec (to-the-right n)))
       (/ viewbox-width (* 2 row-width)))))

(defn node-y
  "Returns the y-coordinate of node n."
  [n]
  (- -18 (* 87 (- (tree-height (count @my-nodes))
                  (tree-height n)))))

(defn node-loc [n]
  [(node-x n) (node-y n)])

(def node-locs
  (vec (map node-loc (range 1 (inc (count @my-nodes))))))

(defn svg-nodes []
  (into [:g]
        (for [node (range (count @my-nodes))]
          (svg-node (str (get @my-nodes node))
                    (first (get node-locs node))
                    (last (get node-locs node))))))

(defn edge
  "Takes 2 vector tuples representing x and y points."
  [from to]
  (let [x1 (first from) x2 (first to)
        y1 (last from) y2 (last to)]
    [:path {:fill "none" :stroke "black"
            :d (str "M" x1 "," y1 "L" x2 "," y2)}]))

(defn edges [nodes]
  (for [child (range 1 (count nodes))]
    [edge (get node-locs (.ceil js/Math (- (/ child 2) 1)))
     (get node-locs child)]))

(defn heap []
  [:svg {:width "100%" :viewBox "0 0 566 305"}
   (into
    [:g {:transform "scale(1,1), rotate(0), translate(4,301)"}
     (edges @my-nodes)
     [svg-nodes]])])

(defn app []
  [:div#app
   [heap]])

(defn render []
  (r/render [app]
            (.getElementById js/document "root")))

(defn ^:dev/after-load start []
  (render)
  (js/console.log "start"))

(defn ^:export init []
  (js/console.log "init")
  (start))

(defn ^:dev/before-load stop []
  (js/console.log "stop"))

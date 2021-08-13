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

(defn parent [node tree]
  (first (filter #(contains? (get tree %) node) (keys tree))))

(parent :2 g3)

(defn node-depth [node tree]
  (loop [n node parents []]
    (if (nil? (parent n tree))
      (count parents)
      (recur (parent n tree) (conj parents (parent n tree))))))

(node-depth :8 g3)

(defn tree-height [tree]
  (inc (apply max (map #(node-depth % tree) (keys tree)))))

(tree-height g3)

(defn leaf-nodes [tree]
  (filter #(empty? (get tree %)) (keys tree)))

(count (leaf-nodes g3))

(.-innerWidth js/window)
(.-innerHeight js/window)

(filter (fn [[node children]] (seq children)) g3)

(def node-locs
  [                [273 -279]
   [215 -192]                            [315 -192]
   [99 -105]        [215 -105]          [315 -105]          [459 -105]
   [27 -18] [99 -18] [171 -18] [243 -18] [315 -18] [387 -18] [459 -18] [531 -18]])

(defonce my-nodes (r/atom [1]))

(reset! my-nodes (vec (range 1 16)))

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

(edge (get node-locs 0) (get node-locs 1))

(into [:g]
      (for [node (range (dec (count @my-nodes)))]
        (edge (get node-locs node) (get node-locs (inc node)))))

(def edges
  [[edge (get node-locs 0) (get node-locs 1)]
   [edge (get node-locs 0) (get node-locs 2)]
   [edge (get node-locs 1) (get node-locs 3)]
   [edge (get node-locs 1) (get node-locs 4)]
   [edge (get node-locs 2) (get node-locs 5)]
   [edge (get node-locs 2) (get node-locs 6)]
   [edge (get node-locs 3) (get node-locs 7)]
   [edge (get node-locs 3) (get node-locs 8)]
   [edge (get node-locs 4) (get node-locs 9)]
   [edge (get node-locs 4) (get node-locs 10)]
   [edge (get node-locs 5) (get node-locs 11)]
   [edge (get node-locs 5) (get node-locs 12)]
   [edge (get node-locs 6) (get node-locs 13)]
   [edge (get node-locs 6) (get node-locs 14)]])

(defn dag []
  [:svg {:width "100%" :viewBox "0 0 566 305"}
   (into
    [:g {:transform "scale(1,1), rotate(0), translate(4,301)"}
     (take (dec (count @my-nodes)) edges)
     [svg-nodes]]
    )])


(defn app []
  [:div#app
   [dag]])

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

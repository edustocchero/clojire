(ns evaluable)

(def ops
  {"*" clojure.core/*
   "/" clojure.core//
   "+" clojure.core/+
   "-" clojure.core/-})

(defprotocol Evaluable
  (evaluate [this]))

(defn- map-evaluate-and-apply [x]
  (let [[l op r] (map evaluate x)]
    (apply op [l r])))

(defrecord Num [num]
  Evaluable
  (evaluate [{this :num}] (bigdec this)))

(defrecord Operation [op]
  Evaluable
  (evaluate [{this :op}] (get ops this)))

(defrecord Factor [factor]
  Evaluable
  (evaluate [{this :factor}]
    (evaluate this)))

(defrecord Term [term]
  Evaluable
  (evaluate [{this :term}]
    (cond
      (vector? this) (map-evaluate-and-apply this)
      :else (evaluate this))))

(defrecord Expr [expr]
  Evaluable
  (evaluate [{this :expr}]
    (cond
      (vector? this) (map-evaluate-and-apply this)
      :else (evaluate this))))

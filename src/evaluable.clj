(ns evaluable)

(def ops
  {"*" clojure.core/*
   "/" clojure.core//
   "+" clojure.core/+
   "-" clojure.core/-})

(defprotocol Evaluable
  (evaluate [this]))

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
      (vector? this) (let [[l op r] (map evaluate this)]
                       (apply op [l r]))
      :else (evaluate this))))

(defrecord Expr [expr]
  Evaluable
  (evaluate [{this :expr}]
    (cond
      (vector? this) (let [[l op r] (map evaluate this)]
                       (apply op [l r]))
      :else (evaluate this))))

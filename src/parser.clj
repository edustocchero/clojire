(ns parser
  (:require [instaparse.core :as i.c]
            [instaparse.transform :as i.t]
            [evaluable :as ev]))

(def grammar "
expr = term
     | term add-op expr
     ;

term = factor
     | factor mul-op term
     ;

factor = number
       | <'('> expr <')'>
       ;

add-op = '+'
       | '-'
       ;

mul-op = '*'
       | '/'
       ;

number = #'-?[0-9]+(.[0-9]+)?';
")

(def parser (i.c/parser grammar {:output-format :hiccup
                                 :auto-whitespace :standard}))

(def transform-map
  {:number ev/->Num
   :mul-op ev/->Operation
   :add-op ev/->Operation
   :factor ev/->Factor
   :term   (fn
             ([term] (ev/->Term term))
             ([factor op term] (ev/->Term [factor op term])))
   :expr   (fn
             ([expr] (ev/->Expr expr))
             ([term op expr] (ev/->Expr [term op expr])))})

(defn parse [source]
  (i.t/transform transform-map (parser source)))

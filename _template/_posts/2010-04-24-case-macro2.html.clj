; @layout  post
; @title   caseマクロ(2)

(p "条件にある値を複数とれるようにしてみたり")

#-CLJ
(defmacro case [base-val & patterns]
 (cons
  'cond
  (fold (fn [[val & more] res]
         (concat
          res
          (list (cond
                 (vector? val) `(or ~@(map #(list '= base-val %) val))
                 (= val :else) val
                 :else `(= ~base-val ~val))
           (first more))))
   ()
   (partition 2 patterns))))
CLJ

(p "usage:")

#-CLJ
(case x
 [1 2 3] "one-three"
 4 "four"
 :else "ng")
CLJ



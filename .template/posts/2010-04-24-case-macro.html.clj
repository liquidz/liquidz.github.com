; @layout post
; @title caseマクロ

(p "てきとーに書いた。ハイライトさせたいな")

#-CLJ
(defmacro case [base-value & patterns]
 (cons
  'cond
  (fold (fn [[val & more] res]
         (concat res (list (if (= val :else) val `(= ~base-value ~val)) (first more)))) (
    (partition 2 patterns)))))
CLJ


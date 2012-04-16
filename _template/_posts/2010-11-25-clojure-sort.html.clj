; @layout post
; @title  clojureのsortでちょっとはまったこと


(p "以下のリストをそれぞれの2番目の値でソートすることを考える")

#-CLJ
(def ls '((bbb 50) (ddd 100) (aaa 20) (ccc 50)))
CLJ

(p "この時clojure 1.2では以下の2つのコードで挙動が同じになってしまう")

#-CLJ
(sort #(> (second%) (second%2)) ls)
; => ((ddd 100) (bbb 50) (ccc 50) (aaa 20))
(sort #(>= (second%) (second%2)) ls)
; => ((ddd 100) (bbb 50) (ccc 50) (aaa 20))
CLJ

(p "つまりソート条件に同値も含むようにしても判別されない。
   ちなみにgaucheで同様のことをやると以下のように同値も判別されるので
   後に出てきたcccがbbbより先になる")

#-CLJ
(sort ls (lambda (x y) (> (cadr x) (cadr y))))
; => ((ddd 100) (bbb 50) (ccc 50) (aaa 20))
(sort ls (lambda (x y) (>= (cadr x) (cadr y))))
; => ((ddd 100) (ccc 50) (bbb 50) (aaa 20))
CLJ

(p "で、何でかなぁと思って" (code clojure.core/sort) "のソースを見ると
ソートの実態は " (code java.util.Arrays) " の " (code sort) " メソッドで、
比較関数は " (code java.util.Comparator) " としてsortメソッドに渡されているだけ。
このComparatorインターフェイスは " (code compare) " というメソッドを持っていて
これは比較した結果を int型 で小さいか、同値か、大きいかを返す。")

(p "それならばということで " (code compare) " メソッドを意識してみると。。")

#-CLJ
(sort #(if(>(second%) (second%2)) -1 1) ls)
; => ((ddd 100) (ccc 50) (bbb 50) (aaa 20))
CLJ

(p "こんな感じでうまくいった。
ソートで同値まで厳密に扱うことはほとんどないけど、忘れないようにメモ。")



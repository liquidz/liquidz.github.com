---
layout: post
title: clojureのsortでちょっとはまったこと
---

# {{page.title}}
<p class="meta">2010-11-25</p>


以下のリストをそれぞれの2番目の値でソートすることを考える

{% highlight clj %}
(def ls '((bbb 50) (ddd 100) (aaa 20) (ccc 50)))
{% endhighlight %}


この時clojure 1.2では以下の2つのコードで挙動が同じになってしまう

{% highlight clj %}
(sort #(> (second%) (second%2)) ls)
; => ((ddd 100) (bbb 50) (ccc 50) (aaa 20))
(sort #(>= (second%) (second%2)) ls)
; => ((ddd 100) (bbb 50) (ccc 50) (aaa 20))
{% endhighlight %}

つまりソート条件に同値も含むようにしても判別されない。
ちなみにgaucheで同様のことをやると以下のように同値も判別されるので
後に出てきたcccがbbbより先になる

{% highlight scm %}
(sort ls (lambda (x y) (> (cadr x) (cadr y))))
; => ((ddd 100) (bbb 50) (ccc 50) (aaa 20))
(sort ls (lambda (x y) (>= (cadr x) (cadr y))))
; => ((ddd 100) (ccc 50) (bbb 50) (aaa 20))
{% endhighlight %}

で、何でかなぁと思って clojure.core/sort のソースを見ると
ソートの実態は java.util.Arraysのsortメソッドで、
比較関数は java.util.Comparator としてsortメソッドに渡されているだけ。
この Comparatorインターフェイスは compare というメソッドを持っていて
これは比較した結果を int型 で小さいか、同値か、大きいかを返す。

それならばということで compare メソッドを意識してみると。。

{% highlight scm %}
(sort #(if(>(second%) (second%2)) -1 1) ls)
; => ((ddd 100) (ccc 50) (bbb 50) (aaa 20))
{% endhighlight %}

こんな感じでうまくいった。
ソートで同値まで厳密に扱うことはほとんどないけど、忘れないようにメモ。


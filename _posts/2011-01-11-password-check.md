---
layout: post
title: パスワードチェックライブラリ作ったった
---

# {{page.title}}
<p class="meta">2011-01-11</p>


自分で使うためのものだけど、パスワードの入力制限を簡単に実装できるライブラリ作ったった。

 * ソース
  - [https://github.com/liquidz/clj-password-check](https://github.com/liquidz/clj-password-check)
 * clojars
  - [http://clojars.org/org.clojars.liquidz/clj-password-check](http://clojars.org/org.clojars.liquidz/clj-password-check)

使い方は、一般的に使われるであろうチェック用関数は用意してあるので、
その中で自サービスで使いたいものを選ぶだけな感じ。

{% highlight clj %}
; アルファベット、数字、記号を含んでいて、文字数が6～10文字
(def mychecker (combine-checkers contains-alphabet? contains-number? contains-symbol? (length-range 6 10)))
{% endhighlight %}

combine-checkersでは各チェック関数をANDで結合する関数。
結合した関数は左から順に評価。
ORで結合したい場合には combine-checkers-or を使えばおk。

で、作ったチェック関数はチェック結果を真偽値で返すだけだけど、
どのチェックで失敗したかを判別したい場合には @last-checker に
最後にチェックした関数の関数名がシンボル型で入ってるのでそれが使える。

{% highlight clj %}
; 文字数3～5かつ、全て同じ文字でないチェック関数
(let [checker (combine-checkers (length-range 3 5) not-same-characters?)]
  (checker "aa") ; false
  (println @last-checker) ; length-range
  ; --
  (checker "aaa") ; false
  (println @last-checker) ; not-same-characters?
  ; --
  (checker "aaaaaa") ; false
  (println @last-checker) ; length-range
  ; --
  (checker "abc")) ; true
{% endhighlight %}

デフォルトで用意してあるチェック用関数は上記のソース(github)を参照のこと。
そんな感じで。





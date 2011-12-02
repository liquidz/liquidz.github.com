---
layout: post
title: Javaのstaticメソッドをclojureのfnに変換してみた
---

# {{page.title}}
<p class="meta">2010-12-16</p>


引数が1つだけであれば

{% highlight clj %}
#(Hogehoge/fuga %)
{% endhighlight %}

みたいにするだけだけど、オーバーロードされてるメソッドは
引数の個数分別々に定義しないといけなくて面倒だったから
そこらへんも考慮してclojureのfnに変換するコードを書いてみた

[https://gist.github.com/743485](https://gist.github.com/743485)

以下、簡単な例

{% highlight clj %}
(ns test (:use jstatic))
(def encode (static-method->fn java.net.URLEncoder/encode))
(println (encode "ほげ"))
; => %E3%81%BB%E3%81%92
(println (encode "ほげ" "EUC-JP"))
; => %A4%DB%A4%B2
(println (apply encode (list "ほげ" "Shift_JIS")))
; => %82%D9%82%B0
{% endhighlight %}

オーバーロードされたメソッドに対してapplyできるのが
ちょっとだけ嬉しいかも？
でもあまり使い道多くないかも



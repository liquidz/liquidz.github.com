---
layout: post
title: clojureでspymemcachedの薄すぎるラッパー書いた
---

# {{page.title}}
<p class="meta">2011-10-16</p>


herokuでmemcached使うのにちょうど良いライブラリが見当たらなかったので、
[spymemcached](http://code.google.com/p/spymemcached/) の set/get のみをラップしたclojureライブラリ書きました。薄い！

 - [https://github.com/liquidz/clj-spymemcached](https://github.com/liquidz/clj-spymemcached)
 - [https://clojars.org/clj-spymemcached](https://clojars.org/clj-spymemcached)


## 使い方

{% highlight clj %}
; 接続
(memcached! :host "localhost" :port 11211)
; 文字列
(cache-set :hello "world") ; default expiration = 3600
(cache-get :hello :default "default value")
; リスト
(cache-set :ls '(1 2 3 4) :expiration 3600)
(= 1 (first (cache-get :ls)))
; マップ
(cache-set :map {:a 1 :b 2})
(= 2 (:b (cache-get :map)))
{% endhighlight %}

キーにキーワードを使えるようにしたくらいで本当ペラッペラ。
まあ自分用ということで。


---
layout: post
title: Plojuctor: Clojure REPL上で動作するプレゼンツールを作ってみた(半分ネタ)
---

# {{page.title}}
<p class="meta">2011-04-11</p>

![thumbnail](/img/post/plojuctor.pn "thumbnail")


元ネタは以下です。

> Here Comes Clojure: A Clojure Talk in Clojure
> [http://bit.ly/hU4qDx](http://bit.ly/hU4qDx)

中身が全部素のテキストだったので、こういうツールがあっても良いんじゃないかなぁ
という軽い気持ちでツールにしてみました。

## Plojuctor

[https://github.com/liquidz/plojuctor](https://github.com/liquidz/plojuctor)

テストコードもドキュメントも書いてない&alpha;バージョンです。
動作がおかしいところもあると思いますがご了承ください。

### 使い方

{% highlight sh %}
$ git clone git://github.com/liquidz/plojuctor.git
$ cd plojuctor
$ lein deps
$ lein repl
user=> (use 'plojuctor.core :reload)
user=> (next-page!)
{% endhighlight %}

スライドの移動は `(next-page!)` , `(prev-page!)` で行います。
`(move-page! N)` でページ番号指定の移動も可です。

### VimClojure

元ネタ同様に単純なClojure REPL上では色付けされません。
VimClojure上のREPLで動作させるとカラースキームによって色付けされます。

### プレゼンの書き方

詳細は plojuctor.core, plojuctor.sentence を参照してください。
気が向いたらドキュメント書きます。
以下は単純な例です。

{% highlight clj %}
(defslide
 (title "スライドタイトル")
 (item "itemは箇条書き"
  (code (println "コードはこんな感じ"))
  "スライドは定義した順に表示"))

(defslide
 (middle-page
  (center-page
   "上下左右中央")))
{% endhighlight %}

## 最後に

基本的なセンテンスは実装したつもりなので、あとは関数なりマクロなりで
自分の使いやすいようにカスタマイズすれば良いかなぁという感じです。
ただ実際のプロジェクタで表示した時に見やすいかは全く保証できませんが、、、


というClojureネタでした。



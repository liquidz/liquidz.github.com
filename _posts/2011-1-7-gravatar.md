---
layout: post
title: Gravatarのライブラリ作ったった
---

# {{page.title}}
<p class="meta">2011-01-07</p>


需要は全然ないと思うけど自分でちょっと使いそうだったから作ったった。
でもGravatarのAPIをそのままラッピングだけの簡単なものデス。

 * ソース
    * [https://github.com/liquidz/clj-gravatar](https://github.com/liquidz/clj-gravatar)
 * clojars
    * [http://clojars.org/org.clojars.liquidz/clj-gravatar](http://clojars.org/org.clojars.liquidz/clj-gravatar)

以下、使用例

{% highlight clj %}
; アバター画像URL取得
(gravatar-image "your@mail.address")
; サイズ指定
(gravatar-image "your@mail.address" :size 24)
; デフォルト画像指定
(gravatar-image "your@mail.address" :default "ttp://hoge.com/fuga.png")
; HTTPS利用
(gravatar-image "your@mail.address" :secure? true)
; ------
; プロフィール取得
(gravatar-profile "your@mail.address")
; HTTPS利用
(gravatar-profile "your@mail.address" :secure? true)
{% endhighlight %}

デフォルト画像指定はURLちゃんと書いたらimgタグに展開されちゃったからわざとh抜いて書いてます。
あれ、でもプロフィール取得のHTTPSって必要なさそうだな。。まぁいいや。



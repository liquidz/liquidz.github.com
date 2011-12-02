---
layout: post
title: 自分用に heroku + clojure + mongodb のテンプレートプロジェクト作った
---

# {{page.title}}
<p class="meta">2011-10-16</p>


**※追記: このテンプレートは以下を参考にしています。**
> Clojure on Heroku with Noir and Mongo in 10 minutes
> [http://thecomputersarewinning.com/post/clojure-heroku-noir-mongo](http://thecomputersarewinning.com/post/clojure-heroku-noir-mongo)

heroku + clojure + mongo の環境作りに
ちょっと時間を使ってしまったのでテンプレートプロジェクトとしてまとめてみました。自分用に。

[https://github.com/liquidz/heroku-template](https://github.com/liquidz/heroku-template)

## 準備

leiningen, git, heroku はインストールしておく

## 使い方(nsがmytestの場合)

{% highlight sh %}
$ git clone git://github.com/liquidz/heroku-template.git mytest
$ cd mytest
$ ./setup.sh mytest
$ heroku create --stack cedar
$ heroku addons:add mongohq:free
$ git push heroku master
{% endhighlight %}

setup.sh は各ファイルの ns を変換するスクリプト

なおローカルで実行する場合には以下で実行すると、reload, stacktrace が有効になります

{% highlight sh %}
$ lein run -m mytest.dev
{% endhighlight %}


あとは煮るなり焼くなり好きにする感じで


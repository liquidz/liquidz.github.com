---
layout: post
title: 実践(?)Compojure
---

# {{page.title}}
<p class="meta">2011-12-03</p>

この記事は[Clojrure Advent Calendar 2011](http://partake.in/events/393770ce-4637-4f07-bc14-a1f5120eab71)の参加記事です。

今回はCompojureでウェブアプリを作る際に使える
ある程度実践的(?)なTipsなどを紹介します。

## Compojureとは

[Compojure](https://github.com/weavejester/compojure)はClojure向けの軽量ウェブフレームワークです。

Clojure版Sinatraのようなフレームワークでウェブアプリをシンプルに記述できることが特徴で、
[ring](https://github.com/mmcgrana/ring)というウェブアプリケーションライブラリがベースになっています。

## Hello World

最初にベースとなるHelloWorldを作ります。
HelloWorldの作り方自体はググれば他にたくさん記事が見つかると思うので
詳細な説明は割愛して、コード内のコメントで軽く補足します。
なおこれから先は[Leiningen](https://github.com/technomancy/leiningen)がインストール済みであることを前提にしています。

プロジェクトの作成

{% highlight sh %}
$ lein new helloworld
$ cd helloworld
{% endhighlight %}

project.clj の編集

{% highlight clj %}
(defproject helloworld "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [compojure "0.6.5"] ; 12/03時点で最新のタグ
                 [ring/ring-jetty-adapter "0.3.11"]]
  :main helloworld.core)
{% endhighlight %}

src/helloworld/core.clj の編集

{% highlight clj %}
(ns helloworld.core
  (:use
    [compojure.core :only [defroutes GET]]
    [compojure.route :only [not-found]]
    [ring.adapter.jetty :only [run-jetty]]))

(defroutes app
  (GET "/" req "hello world")
  ; defroutesは定義した順に処理するためnot-foundは最後に書く
  (not-found "NOT FOUND"))

(defn -main []
  ; heroku向けのport取得
  (let [port (Integer/parseInt (get (System/getenv) "PORT" "8080"))]
    (run-jetty app {:port port})))
{% endhighlight %}

実行

{% highlight sh %}
$ lein deps
$ lein run
$ open http://localhost:8080
{% endhighlight %}

Hello World!!

## 静的ファイルを扱う

無事HelloWorldが表示できました。

あとはhiccupやenliveといったテンプレートエンジンを使えば動的な画面は問題ないでしょう。
では静的な画面は？というと以下のようにします。

project.clj に以下を追加

{% highlight clj %}
:web-content "public"
{% endhighlight %}

src/helloworld/core.clj のルートを編集

{% highlight clj %}
(ns helloworld.core
  (:use
    [compojure.core :only [defroutes GET]]
    ; filesを追加
    [compojure.route :only [not-found files]]
    [ring.adapter.jetty :only [run-jetty]]))

(defroutes app
  (GET "/" req "hello world")
  (files "/") ; publicディレクトリを"/"にひもづける
  (not-found "NOT FOUND"))
{% endhighlight %}

静的ファイルを用意

{% highlight sh %}
$ pwd
プロジェクトディレクトリ
$ mkdir public; cd public
$ echo NEKO > neko.txt
{% endhighlight %}

Jettyの再起動を再起動して http://localhost:8080/neko.txt へアクセスすれば
静的ファイルを参照できます。

## 開発を効率化

先ほどの静的ファイルへの対応では修正後にJettyを再起動しました。
でも修正の度に再起動するのは効率的ではありません。

そこでring-develの`reload`と`stacktrace`を使いましょう。

project.clj に追加

{% highlight clj %}
:dev-dependencies [[ring/ring-devel "0.3.11"]]
{% endhighlight %}

src/helloworld/core.clj の修正

{% highlight clj %}
(ns helloworld.core
  (:use
    [compojure.core :only [defroutes GET]]
    [compojure.route :only [not-found files]]
    [ring.middleware reload stacktrace]
    [ring.adapter.jetty :only [run-jetty]]))

(defn index
  "/ にアクセスされたときの処理"
  [req]
  "hello world")

(defroutes main-route
  (GET "/" req (index req)) ; 処理を関数に
  (GET "/err" _ (throw (Exception.))) ; stacktraceの確認用
  (files "/")
  (not-found "NOT FOUND"))

(defroutes app
  (-> main-route
    (wrap-reload '[helloworld.core])
    wrap-stacktrace))
{% endhighlight %}

実行

{% highlight sh %}
$ lein deps
$ lein run
{% endhighlight %}

Jettyの再起動に関係なく index の戻り値が反映されるのが確認できたでしょうか？
また stacktrace を使うと /err にアクセスした際に、画面上に例外の内容を表示させることができます。

なおreloadですが、`defroutes`内の変更は反映されないようなので、
routeの変更の際にはJettyの再起動が必要です。(この点、対処方法があれば誰か教えてください。)


## Middlewareで拡張

先ほどの reload, stacktrace はringのmiddlewareと言われるもので、
これらを使うとCompojureの挙動を拡張することができます。

主要なmiddlewareは以下の通りです。

### ring.middleware.params/wrap-params

QueryString, POSTデータを `{:params request}` に展開

{% highlight clj %}
(defroutes main-routes
  (GET "/" {params :params}
    (get params "get_parameter")))

(defroutes app
  (-> main-routes wrap-params))
{% endhighlight %}

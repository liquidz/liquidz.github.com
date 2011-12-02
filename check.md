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

 - 実行

{% highlight sh %}
$ lein deps
$ lein run
$ open http://localhost:8080
{% endhighlight %}

{% highlight clj %}
{% endhighlight %}

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
(ns hogefuga.core
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


 - static file
 - not found
 - port with env

## Middlewareで拡張
## テストする
## dev webapi with compojure
### ring middleware

 wrap系は処理をラップした関数を返すので
 ->, ->>で適用する場合には逆順に処理されるので注意

 ex)
    (wrap-A (wrap-B (wrap-C app))) ; A->B->Cの順で処理される
    (-> app wrap-A wrap-B wrap-C)  ; C->B->Aの順で処理される
    (-> app wrap-params wrap-keyword-params) ; NG: paramsの前にkeyword-paramsが処理される
    (-> app wrap-keyword-params wrap-params) ; OK: paramsのあとにkeyword-paramsが処理される

 - ring.middleware.params/wrap-params
    QueryString, POSTデータを {:params request} に展開する

 - ring.middleware.nested-params/wrap-nested-params
    以下のような添字付きのパラメータをネストしたmapに展開する
    展開できる階層は1階層のネストまで
    ?a[b]=c&a[d]=e
        {"a" {"d" "e", "b" "c"}}
 - ring.middleware.keyword-params/wrap-keyword-params
    params前提
    パラメータ名をStringからKeywordに変換

 - ring.middleware.session
 - ring.middleware.flash
 セッションを使って一時的なメッセージを保存する
 リダイレクト先でちょろっとメッセージを表示したいときとか
 あと以下のような凡ミスはしないように
 http://twitter.com/#!/uochan/status/141546228574982144

 - ring.middleware.cookies
 - ring.middleware.file/file
    ; with lein-prj-dir/public/
    (wrap-file handler "public")

 - compojure.handler/api
  - 以下3つをラップ
   - wrap-params
   - wrap-nested-params
   - wrap-keyword-params

devlop

 - reload
  - defroutes内の変更は反映されない模様
 - wrap-stacktrace
  - 例外が発生した際にStacktraceを画面に出力

### compojure test
 - ring-mock



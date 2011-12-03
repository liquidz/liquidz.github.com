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


### ring.middleware.nested-params/wrap-nested-params

添字付きのパラメータをネストしたマップに展開。要 `wrap-params`。
なお展開できる階層は1階層のネストまで

{% highlight clj %}
(defroutes main-routes
  (GET "/" {params :params} (str params)))

(defroutes app
  (-> main-routes wrap-nested-params wrap-params))
{% endhighlight %}

{% highlight sh %}
$ open "http://localhost:8080/?a[b]=c&a[d]=e"
{% endhighlight %}

{% highlight clj %}
{"a" {"d" "e", "b" "c"}}
{% endhighlight %}

### ring.middleware.keyword-params/wrap-keyword-params

パラメータ名をStringからKeywordに変換。`wrap-params`, `wrap-nested-params`と一緒に使う

{% highlight clj %}
(defroutes main-routes
  ; 分配束縛が楽
  (GET "/" { {:keys [param1 param2]} :params}
    (str "param1 = " param1 ", param2 = " param2)))

(defroutes app
  (-> main-routes wrap-keyword-params wrap-params))
{% endhighlight %}


### ring.middleware.session

セッションを扱う。
セッション情報はリクエストの :session キーで渡される

{% highlight clj %}
(ns helloworld.core
  (:use
    ..省略..
    [ring.util.response :only [redirect]]))

(defroutes main-routes
  (GET "/set/:vlue" [value]
    ; セッションのセットはレスポンスに :session を指定するだけ
    (assoc (redirect "/") :session {:value value}))

  (GET "/" { {:keys [value], :or {value "no data"}} :session}
    (str "value = " value)))

(defroutes app
  (-> main-routes wrap-session))
{% endhighlight %}

{% highlight sh %}
$ open "http://localhost:8080/set/helloworld"
{% endhighlight %}



### ring.middleware.flash
セッションを使って一時的なメッセージを保存。要 `wrap-session`。
リダイレクト先でちょろっとメッセージを表示したいときとかに使う

{% highlight clj %}
(ns helloworld.core
  (:use
    ..省略..
    [ring.util.response :only [redirect]]))

(defroutes main-routes
  (GET "/set/:value" [value]
    ; flashのセットはレスポンスに :flash で指定
    (assoc (redirect "/") :flash value))
  (GET "/" {flash :flash}
    (str "flash = " flash)))

(defroutes app
  (-> main-routes wrap-flash wrap-session))
{% endhighlight %}

flashをセット

{% highlight clj %}
$ open "http://localhost:8080/set/helloworld"
{% endhighlight %}

セットされてるのが確認できますが、もう一度アクセスすると一時的な情報なので削除されています。

{% highlight clj %}
$ open "http://localhost:8080/"
{% endhighlight %}

なお以下のような凡ミスはしないようご注意を

[http://twitter.com/#!/uochan/status/141546228574982144](http://twitter.com/#!/uochan/status/141546228574982144)

### ring.middleware.cookies

クッキーを扱う。クッキー情報はリクエストの :cookies キーで渡される

{% highlight clj %}
(ns helloworld.core
  (:use
    ..省略..
    [ring.util.response :only [redirect]]))

(defroutes main-routes
  (GET "/set/:value" [value]
    ; クッキーの設定はレスポンスの :cookies キーで行う
    (assoc (redirect "/") :cookies {:hello {:value value :path "/"}}))
  (GET "/" {cookies :cookies}
    (str cookies)))

(defroutes app
  (-> main-routes wrap-cookies))
{% endhighlight %}

クッキー設定時には上記以外に `:domain` `:port` `:max-age` `:expires` `:secure` `:http-only` が使えます。
詳細は以下のソース末尾を見ると良いです。

[https://github.com/mmcgrana/ring/blob/master/ring-core/src/ring/middleware/cookies.clj](https://github.com/mmcgrana/ring/blob/master/ring-core/src/ring/middleware/cookies.clj)


### ring.middleware.file/file

静的ファイルを扱います。
こちらだと project.clj に `:web-content` を指定しなくてもディレクトリを割り当てられます。

{% highlight clj %}
(defroutes app
  (-> main-routes (wrap-file "public")))
{% endhighlight %}

### Middlewareのちょっとした注意
wrap系は処理をラップした関数を返すので
`->`, `->>`で適用する場合には逆順に処理されるので注意してください。

{% highlight clj %}
(wrap-A (wrap-B (wrap-C app))) ; A->B->Cの順で処理される
(-> app wrap-A wrap-B wrap-C)  ; C->B->Aの順で処理される
(-> app wrap-params wrap-keyword-params) ; NG: paramsの前にkeyword-paramsが処理される
(-> app wrap-keyword-params wrap-params) ; OK: paramsのあとにkeyword-paramsが処理される
{% endhighlight %}

### 面倒くさい

route毎にどのmiddlewareをラップすれば良いのかわからない！面倒くさい！
という人用に(?)、Compojureでは`site`、`api`を用意しています。

#### compojure.handler/site

HTMLを出力するroute向け。以下7つをラップ

 - wrap-session
 - wrap-flash
 - wrap-cookies
 - wrap-multipart-params
 - wrap-params
 - wrap-nested-params
 - wrap-keyword-params

#### compojure.handler/api

ウェブAPI向け。以下3つをラップ

 - wrap-params
 - wrap-nested-params
 - wrap-keyword-params

{% highlight clj %}
{% endhighlight %}


## テストする

ここまでに紹介したmiddlewareを使えば一般的なウェブアプリであれば
問題なく開発できるかと思います。

最後にテストです。例えば以下のようなAPIのテストを書いてみましょう。

project.clj の dependencies に以下を追加

{% highlight clj %}
[org.clojure/data.json "0.1.1"]
{% endhighlight %}

src/helloworld/core.clj

{% highlight clj %}
(ns helloworld.core
  (:use
    ..省略..
    [compojure.handler :only [api]]
    [clojure.data.json :only [json-str]]))

(defn word-count [text]
  (->> text (re-seq #"\w+") (map (fn [x] {x 1})) (apply (partial merge-with +))))

(defroutes api-route
  (GET "/wc" { {:keys [text]} :params}
    (json-str (word-count text))))

(defroutes app
  (api api-route))
{% endhighlight %}

HTMLを返すルートのテストは難しいですが、APIでデータを返すルートのテストは
`ring-mock` を使うことで簡単に記述することができます。

project.clj の dev-dependencies に以下を追加

{% highlight clj %}
[ring-mock "0.1.1"]
{% endhighlight %}

test/helloworld/test/core.clj

{% highlight clj %}
(ns helloworld.test.core
  (:use
    helloworld.core
    clojure.test
    [clojure.data.json :only [read-json]]
    [ring.mock.request :only [request]]))

(deftest word-count-test
  (let [; ring.mock.request/request でレスポンスを取得
        ; 第3引数のマップはQueryStringに展開される
        res (app (request :get "/wc" {:text "hello world hello"}))
        ; JSON形式からマップに変換
        body (-> res :body read-json)]
    ; are って便利
    (are [x y] (= x y)
      200 (:status res)
      2 (:hello body)
      1 (:world body))))
{% endhighlight %}

このような感じで `ring-mock` を使うと
関数のテストだけではカバーできない
実際にリクエストで得られる結果もテストできます。

## 最後に

長文になってしまいましたがいかがでしたでしょうか？
少しでも Compojure でのウェブアプリ開発に役立てれば幸いです。

具体的なコード例は以下にコミットしてあります。
(middlewareまわりは面倒だったので書いてないです。もし要望があれば書きます)

[https://github.com/liquidz/practical-compojure-sample](https://github.com/liquidz/practical-compojure-sample)

なお間違いや、より良い方法などあればご指摘ください！



; @layout post
; @title  実践(?)Compojure


(p "この記事は"
   (html/link "Clojrure Advent Calendar 2011" "http://partake.in/events/393770ce-4637-4f07-bc14-a1f5120eab71")
   "の参加記事です。")

(p "今回はCompojureでウェブアプリを作る際に使える
   ある程度実践的(?)なTipsなどを紹介します。")

[:strong "長文なのでご注意ください"]

(h2 "Compojureとは")

(p (html/link "Compojure" "https://github.com/weavejester/compojure")
   "はClojure向けの軽量ウェブフレームワークです。")

(p "Clojure版Sinatraのようなフレームワークでウェブアプリをシンプルに記述できることが特徴で、"
   (html/link "ring" "https://github.com/mmcgrana/ring") "というウェブアプリケーションライブラリがベースになっています。")

(h2 "Hello World")

(p "最初にベースとなるHelloWorldを作ります。
   HelloWorldの作り方自体はググれば他にたくさん記事が見つかると思うので
   詳細な説明は割愛して、コード内のコメントで軽く補足します。")

(p "なおこれから先は"
   (html/link "Leiningen" "https://github.com/technomancy/leiningen")
   "がインストール済みであることを前提にしています。")

(p "プロジェクトの作成")

#-SH
$ lein new helloworld
$ cd helloworld
SH

(p "project.clj の編集")

#-CLJ
(defproject helloworld "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [compojure "0.6.5"] ; 12/03時点で最新のタグ
                 [ring/ring-jetty-adapter "0.3.11"]]
  :main helloworld.core)
CLJ

(p "src/helloworld/core.clj の編集")

#-CLJ
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
CLJ

(p "実行")

#-SH
$ lein deps
$ lein run
$ open http://localhost:8080
SH

(p "Hello World!!")

(h2 "静的ファイルを扱う")

(ps "無事HelloWorldが表示できました。"

    "あとはhiccupやenliveといったテンプレートエンジンを使えば動的な画面は問題ないでしょう。
    では静的な画面は？というと以下のようにします。"

    "project.clj に以下を追加")

#-CLJ
:web-content "public"
CLJ

(p "src/helloworld/core.clj のルートを編集")

#-CLJ
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
CLJ

(p "静的ファイルを用意")

#-SH
$ pwd
プロジェクトディレクトリ
$ mkdir public; cd public
$ echo NEKO > neko.txt
SH

(p "Jettyの再起動を再起動して http://localhost:8080/neko.txt へアクセスすれば
   静的ファイルを参照できます。")

(h2 "開発を効率化")

(p "先ほどの静的ファイルへの対応では修正後にJettyを再起動しました。
   でも修正の度に再起動するのは効率的ではありません。")

(p "そこでring-develの" (html/code reload) " と " (html/code stacktrace) " を使いましょう。")

(p "project.clj に追加")

#-CLJ
:dev-dependencies [[ring/ring-devel "0.3.11"]]
CLJ

(p "src/helloworld/core.clj の修正")

#-CLJ
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
CLJ

(p "実行")

#-SH
$ lein deps
$ lein run
SH

(p "Jettyの再起動に関係なく index の戻り値が反映されるのが確認できたでしょうか？
   また " (html/code stacktrace) " を使うと /err にアクセスした際に、画面上に例外の内容を表示させることができます。")

(p "なお " (html/code reload) " ですが、" (html/code defroutes) " 内の変更は反映されないようなので、
routeの変更の際にはJettyの再起動が必要です。(この点、対処方法があれば誰か教えてください。)")


(h2 "Middlewareで拡張")

(ps "先ほどの reload, stacktrace はringのmiddlewareと言われるもので、
    これらを使うとCompojureの挙動を拡張することができます。"

    "主要なmiddlewareは以下の通りです。")


(h3 "ring.middleware.params/wrap-params")

(p "QueryString, POSTデータを " (html/code {:params request}) " に展開")

#-CLJ
(defroutes main-routes
  (GET "/" {params :params}
    (get params "get_parameter")))

(defroutes app
  (-> main-routes wrap-params))
CLJ


(h3 "ring.middleware.nested-params/wrap-nested-params")

(p "添字付きのパラメータをネストしたマップに展開。要 " (html/code wrap-params) "。")
(p "なお展開できる階層は1階層のネストまで")

#-CLJ
(defroutes main-routes
  (GET "/" {params :params} (str params)))

(defroutes app
  (-> main-routes wrap-nested-params wrap-params))
CLJ

#-SH
$ open "http://localhost:8080/?a[b]=c&a[d]=e"
SH

#-CLJ
{"a" {"d" "e", "b" "c"}}
CLJ

(h3 "ring.middleware.keyword-params/wrap-keyword-params")

(p "パラメータ名をStringからKeywordに変換。" (html/code wrap-params) ", " (html/code wrap-nested-params) "と一緒に使う")

#-CLJ
(defroutes main-routes
  ; 分配束縛が楽
  (GET "/" { {:keys [param1 param2]} :params}
    (str "param1 = " param1 ", param2 = " param2)))

(defroutes app
  (-> main-routes wrap-keyword-params wrap-params))
CLJ


(h3 "ring.middleware.session")

(p "セッションを扱う。
   セッション情報はリクエストの :session キーで渡される")

#-CLJ
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
CLJ

#-SH
$ open "http://localhost:8080/set/helloworld"
SH



(h3 "ring.middleware.flash")
(p "セッションを使って一時的なメッセージを保存。要 " (html/code wrap-session) "。")

(p "リダイレクト先でちょろっとメッセージを表示したいときとかに使う")

#-CLJ
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
CLJ

(p "flashをセット")

#-SH
$ open "http://localhost:8080/set/helloworld"
SH

(p "セットされてるのが確認できますが、もう一度アクセスすると一時的な情報なので削除されています。")

#-SH
$ open "http://localhost:8080/"
SH

(p "なお以下のような凡ミスはしないようご注意を")

(html/link "http://twitter.com/#!/uochan/status/141546228574982144")

(h3 "ring.middleware.cookies")

(p "クッキーを扱う。クッキー情報はリクエストの :cookies キーで渡される")

#-CLJ
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
CLJ

(p "クッキー設定時には上記以外に
   :domain, :port, :max-age, :expires, :secure, :http-only
   が使えます。")

(p "詳細は以下のソース末尾を見ると良いです。")

(html/link "https://github.com/mmcgrana/ring/blob/master/ring-core/src/ring/middleware/cookies.clj")

(h3 "ring.middleware.file/file")

(p "静的ファイルを扱います。こちらだと project.clj に "
   (html/code :web-content) " を指定しなくてもディレクトリを割り当てられます。")

#-CLJ
(defroutes app
  (-> main-routes (wrap-file "public")))
CLJ

(h3 "Middlewareのちょっとした注意")
(p "wrap系は処理をラップした関数を返すので
   " (html/code ->) ", " (html/code ->>) "で適用する場合には逆順に処理されるので注意してください。")

#-CLJ
(wrap-A (wrap-B (wrap-C app))) ; A->B->Cの順で処理される
(-> app wrap-A wrap-B wrap-C)  ; C->B->Aの順で処理される
(-> app wrap-params wrap-keyword-params) ; NG: paramsの前にkeyword-paramsが処理される
(-> app wrap-keyword-params wrap-params) ; OK: paramsのあとにkeyword-paramsが処理される
CLJ

(h3 "面倒くさい")

(p "route毎にどのmiddlewareをラップすれば良いのかわからない！面倒くさい！
   という人用に(?)、Compojureでは" (html/code site) "、" (html/code api) "を用意しています。")

(h4 "compojure.handler/site")

(p "HTMLを出力するroute向け。以下7つをラップ")

(html/ul
  ["wrap-session"
   "wrap-flash"
   "wrap-cookies"
   "wrap-multipart-params"
   "wrap-params"
   "wrap-nested-params"
   "wrap-keyword-params"])

(h4 "compojure.handler/api")

(p "ウェブAPI向け。以下3つをラップ")

(html/ul
  ;#(html/code %)
  ["wrap-params"
   "wrap-nested-params"
   "wrap-keyword-params"])


(h2 "テストする")

(ps "ここまでに紹介したmiddlewareを使えば一般的なウェブアプリであれば
    問題なく開発できるかと思います。"

    "最後にテストです。例えば以下のようなAPIのテストを書いてみましょう。"

    "project.clj の dependencies に以下を追加")

#-CLJ
[org.clojure/data.json "0.1.1"]
CLJ

(p "src/helloworld/core.clj")

#-CLJ
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
CLJ

(p "HTMLを返すルートのテストは難しいですが、APIでデータを返すルートのテストは"
   (html/code ring-mock) " を使うことで簡単に記述することができます。")

(p "project.clj の dev-dependencies に以下を追加")

#-CLJ
[ring-mock "0.1.1"]
CLJ

(p "test/helloworld/test/core.clj")

#-CLJ
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
CLJ

#-SH
$ lein deps
$ lein test
Testing helloworld.test.core
Ran 1 tests containing 3 assertions.
0 failures, 0 errors.
SH

(p "このような感じで " (html/code ring-mock) " を使うと"
   "関数のテストだけではカバーできない
   実際にリクエストで得られる結果もテストできます。")

(h2 "最後に")

(ps "長文になってしまいましたがいかがでしたでしょうか？
    少しでも Compojure でのウェブアプリ開発に役立てれば幸いです。"

    "具体的なコード例は以下にコミットしてあります。
    (middlewareまわりは面倒だったので書いてないです。もし要望があれば書きます)")

(html/link "https://github.com/liquidz/practical-compojure-sample")

(p "なお間違いや、より良い方法などあればご指摘ください！")



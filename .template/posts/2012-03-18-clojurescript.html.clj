; @layout post
; @title  全部Clojureのターン(noir+noir-cljs)

(p "node.jsがサーバ、クライアントをJSだけで書けるのに対して、Clojureも"
   (link "ClojureScript" "https://github.com/clojure/clojurescript")
   "を使えばサーバ、クライアントを1つの言語で書けます。")

(p "さらにClojureには"
   (link "hiccup" "https://github.com/weavejester/hiccup")
   "というS式を使ったテンプレートエンジンがあるので"
   [:strong "サーバ(ビューも含め)+クライアントを全部Clojureで書けます。"])

[:p {:class "paragraph" :style "font-size: 50px;"}
 "まさに全部Clojureのターン！"]

(p "今回は "
   (link "noir" "http://webnoir.org/")
   " と "
   (link "noir-cljs" "https://github.com/ibdknox/noir-cljs")
   " を使って、単純にボタンを押してテキストを変更するだけのアプリ(?)を作るまでの手順です。")


(h2 "noirのインストール")

(p "まずはnoirをインストールしましょう。")

#-SH
lein plugin install lein-noir 1.2.1
SH

(p "次にサンプルアプリを作成します。")

#-SH
lein noir new zenbu-clojure
cd zenbu-clojure
SH

(h2 "noir-cljsの設定")

(p "noir-cljsのREADMEに沿って project.clj を修正します。")

(h3 "project.clj")
#-CLJ
(defproject zenbu-clojure "0.1.0-SNAPSHOT"
            :description "FIXME: write this!"
            :dependencies [[org.clojure/clojure "1.3.0"]
                           [noir "1.2.1"]
                           [noir-cljs "0.2.5"]] ; 追加
            :main ^{:skip-aot true} zenbu-clojure.server) ; aotコンパイルをスキップ
CLJ

(p "次に src ディレクトリ配下の .cljs ファイルが変更された時に自動的にコンパイルが走るよう
   server.clj を変更します。")

(h3 "src/zenbu_clojure/server.clj")
#-CLJ
(ns zenbu-clojure.server
  (:require [noir.server :as server]))

(server/load-views "src/zenbu_clojure/views/")

(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. (get (System/getenv) "PORT" "8080"))]
    (noir.cljs.core/start mode) ; ここを追加
    (server/start port {:mode mode
                        :ns 'zenbu-clojure})))
CLJ

(h2 "テンプレート修正")

(p "まずはコンパイルされた cljs を読み込むようレイアウトを修正します")

(h3 "src/zenbu_clojure/views/common.clj")

#-CLJ
(ns zenbu-clojure.views.common
  (:use [noir.core :only [defpartial]]
        [hiccup.page-helpers :only [include-css html5]])
  (:require [noir.cljs.core :as noir-cljs])) ; 追加

(defpartial layout [& content]
            (html5
              [:head
               [:title "zenbu-clojure"]
               (include-css "/css/reset.css")]
              [:body
               [:div#wrapper
                content]
               (noir-cljs/include-scripts) ; コンパイルしたcljsを読み込み
               ]))
CLJ

(p "次に welcome ページにちょっと手を入れます")

(h3 "src/zenbu_clojure/views/welcome.clj")

#-CLJ
(ns zenbu-clojure.views.welcome
  (:require [zenbu-clojure.views.common :as common]
            [noir.content.getting-started])
  (:use [noir.core :only [defpage]]
        [hiccup.core :only [html]]))

(defpage "/" [] ; index ページに変更
         (common/layout
           [:button {:id "btn"} "全部..."] ; ボタンを追加
           [:p {:id "text"} "Welcome to zenbu-clojure"] ; id属性を追加
           ))
CLJ

(h2 "ClojureScript作成")

(ps "最後にClojureScriptを書きます。"

    "なお個人的にちょっとハマった点として
    noir-cljs 0.2.5 では \"cljs\" という名前のディレクトリは
    ClojureScriptとして認識されてしまうようでサーバ起動時にエラーになってしまいます。")

(ps "(同作者の" (link "watchtower" "https://github.com/ibdknox/watchtower")
    "というライブラリによる挙動で、pull requestだけ出してあるのでもしマージされれば問題なくなるかもしれません)")

(warn "2012-04-08 追記: マージしていただいたので ver 0.1.1 を使えば問題ありません")


(h3 "src/zenbu_clojure/mycljs/core.cljs")

#-CLJ
(ns zenbu-clojure.mycljs.core
  (:reuiqre [clojure.browser.event :as event]
            [clojure.browser.dom :ad dom]))

(defn click-event
  "#btnクリック時のクリックイベント"
  []
  (dom/set-text (dom/get-element "text") "Clojureのターン!"))

(defn init
  "onload時に呼ばれる関数"
  []
  (event/listen (dom/get-element "btn") :click click-event))

; onloadイベントを追加
(goog.events/listen js/window goog.events.EventType/LOAD init)
CLJ

(p "ここまできたらサーバを起動してみましょう。")

#-SH
lein run
SH

(p "ポート指定していなければ http://localhost:8080 で確認できます。")

(h2 "最後に")

(ps "今回はアプリというのにはおこがましいくらい単純なものでしたが、
    全部Clojureで書けることはお見せできたと思います。"

    "私自身、まだClojureScriptにあまり慣れていないので要勉強ですが、
    もう少しまともなアプリが書けるようになったらまたまとめてみようかと思います。")

[:p {:class "paragraph" :style "color: #777;"}
"なおここまで書いて "
 (link "cljs-template" "https://github.com/ibdknox/cljs-template")
 " に気づきました、、、"
 "こっちを使えばもっと簡単にできそうなのであとで試してみたいと思います。"]


(warn "追記")

(p "参考ページを書き忘れていました。。ClojureScriptの記述については以下を参考にさせていただきました。")

(links
  "Clojure Scriptを使ってみる"    "http://dev.classmethod.jp/ria/clojurescript/"
  "DOM Element を def で束縛する" "http://qiita.com/items/1548")


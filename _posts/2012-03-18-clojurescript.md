---
layout: post
title: 全部Clojureのターン(noir+noir-cljs)
---

# {{page.title}}
<p class="meta">2012-03-18</p>

node.jsがサーバ、クライアントをJSだけで書けるのに対して、
Clojureも[ClojureScript](https://github.com/clojure/clojurescript)を使えばサーバ、クライアントを1つの言語で書けます。

さらにClojureには[hiccup](https://github.com/weavejester/hiccup)というS式を使ったテンプレートエンジンがあるので
<strong>サーバ(ビューも含め)+クライアントを全部Clojureで書けます。</strong>

<p style="font-size: 50px;">まさに全部Clojureのターン！</p>

今回は [noir](http://webnoir.org/) と [noir-cljs](https://github.com/ibdknox/noir-cljs) を使って、
単純にボタンを押してテキストを変更するだけのアプリ(?)を作るまでの手順です。


## noirのインストール

まずはnoirをインストールしましょう。

{% highlight sh %}
lein plugin install lein-noir 1.2.1
{% endhighlight %}

次にサンプルアプリを作成します。

{% highlight sh %}
lein noir new zenbu-clojure
cd zenbu-clojure
{% endhighlight %}

## noir-cljsの設定

noir-cljsのREADMEに沿って project.clj を修正します。

 - project.clj
{% highlight clojure %}
(defproject zenbu-clojure "0.1.0-SNAPSHOT"
            :description "FIXME: write this!"
            :dependencies [[org.clojure/clojure "1.3.0"]
                           [noir "1.2.1"]
                           [noir-cljs "0.2.5"]] ; 追加
            :main ^{:skip-aot true} zenbu-clojure.server) ; aotコンパイルをスキップ
{% endhighlight %}

次に src ディレクトリ配下の .cljs ファイルが変更された時に自動的にコンパイルが走るよう
server.clj を変更します。

 - src/zenbu_clojure/server.clj
{% highlight clojure %}
(ns zenbu-clojure.server
  (:require [noir.server :as server]))

(server/load-views "src/zenbu_clojure/views/")

(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. (get (System/getenv) "PORT" "8080"))]
    (noir.cljs.core/start mode) ; ここを追加
    (server/start port {:mode mode
                        :ns 'zenbu-clojure})))
{% endhighlight %}

## テンプレート修正

まずはコンパイルされた cljs を読み込むようレイアウトを修正します

 - src/zenbu_clojure/views/common.clj
{% highlight clojure %}

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
{% endhighlight %}

次に welcome ページにちょっと手を入れます

 - src/zenbu_clojure/views/welcome.clj
{% highlight clojure %}
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
{% endhighlight %}

## ClojureScript作成

最後にClojureScriptを書きます。

なお個人的にちょっとハマった点として
noir-cljs 0.2.5 では "cljs" という名前のディレクトリは
ClojureScriptとして認識されてしまうようでサーバ起動時にエラーになってしまいます。

(同作者の[watchtower](https://github.com/ibdknox/watchtower)というライブラリによる挙動で、pull requestだけ出してあるので
もしマージされれば問題なくなるかもしれません)

 - src/zenbu_clojure/mycljs/core.cljs
{% highlight clojure %}
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
{% endhighlight %}

ここまできたらサーバを起動してみましょう。

{% highlight sh %}
lein run
{% endhighlight %}

ポート指定していなければ http://localhost:8080 で確認できます。

## 最後に

今回はアプリというのにはおこがましいくらい単純なものでしたが、
全部Clojureで書けることはお見せできたと思います。

私自身、まだClojureScriptにあまり慣れていないので要勉強ですが、
もう少しまともなアプリが書けるようになったらまたまとめてみようかと思います。

<p style="color: #777;">なおここまで書いて <a href="https://github.com/ibdknox/cljs-template">cljs-template</a> に気づきました、、、
こっちを使えばもっと簡単にできそうなのであとで試してみたいと思います。</p>

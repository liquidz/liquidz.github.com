; @layout post
; @title  Clojureのテンプレートエンジン「cuma」を作ったクマー

(p "Clojureでシンプルなテンプレートエンジンを作りました")

(blockquote
  "cuma: Extensible micro template engine for Clojure"
  (link "https://github.com/liquidz/cuma"))


(h2 "動機")
(p "もともと、[misaki](https://gihub.com/liquidz/misaki/)などでは[clostache](https://github.com/fhd/clostache)というテンプレートエンジンを使ってました。")
   
(p "これは[mustache](http://mustache.github.io/)のClojure版で、これはこれでとても素晴らしいものなのですが
   [misaki-markdown](https://gihub.com/liquidz/misaki-markdown/) を開発する上で
   以下2つの問題にぶつかりました以下2つの問題にぶつかりました")

(ul ["置き換える文字列中にテンプレートの表記があると正しく置き換えられない"
    "機能拡張ができない"])

(p "前者については送った[pull-request](https://github.com/fhd/clostache/pull/28)がマージされたので解消されていますが、後者についてはそもそもmustacheが *Logic-less templates* を謳っているので
   方向性が違うのかと思います。")

(p "そこでシンプルなフォーマットを保ちつつ、clojureで機能拡張できるものとして[cuma](https://github.com/liquidz/cuma)を作りました。")

(p "なぜ「cuma」なのかというと、別に自分がクマ好きでカバンとか携帯にクマのぬいぐるみを付けてるからとかそういうのではなく '**C**lo**U**reのte**M**pl**A**te'からテキトーに文字をピックアップしただけです。偶然です。")


(h2 "特徴")
(p "Clostacheと同じことは同じようにできます。インラインでは`$(...)`、複数行にまたがるものは`@(...) ... @(/...)` というフォーマットになっています。他の例は[github](https://github.com/liquidz/cuma)のページを参照してください。")
##CLJ
(render "$(x)" {:x "hello"})
;=> hello

(render "$(escape x)" {:x "<h1>hello</h1>"})
;=> &lt;h1&gt;hello&lt;/h1&gt;

(render "@(if flag) hello @(/if)" {:flag true})
;=> hello

(render "@(for arr) $(.) @(/for)" {:arr [1 2 3]})
;=> 1 2 3
CLJ

(h3 "拡張機能")
(p "cumaでは上記フォーマットを保ったまま機能拡張することができます。
   具体的には上記の `escape`, `if`, `for` にあたるものを自分で作ることができますし、
   `escape`, `if`, `for` 自体も本体に組み込まれていますが拡張機能として実装されています。")

(link "https://github.com/liquidz/cuma/blob/master/src/cuma/extension/core.clj")

(p "cumaでは実行時に `cuma.extension.*` という名前空間を検索し、その中のpublicな関数を
   拡張機能としてロードし、テンプレート内で使うことができます。")

##CLJ
(ns cuma.extension.hello)

(defn hello [data s]
  (str "hello " s))
CLJ
##CLJ
(render "$(hello x)" {:x "world"})
;=> hello world
CLJ

(h3 "パフォーマンス")
(p "シンプルなテンプレートなのに動作が遅かったら論外かと思います。cumaでは、misaki, misaki-markdownで使っているclostacheをcumaに移行することを考え、clostacheより高速に動作するようにしています。")

(img "/img/post/2013-04-14/cuma_performance.png")

(p "なお計測に使ったコードは[gist](https://gist.github.com/liquidz/5381090)に貼り付けておきました。正確でない点があればご指摘いただければと思います。")

(h2 "最後に")
(p "自分用に作ったので小規模向けではありますが、フォーマットはシンプル、かつ拡張機能による柔軟性を意識して作ったので、そこそこ使いやすいのではないかなと思っています。
   ご意見、要望などあれば[github](https://github.com/liquidz/cuma)のissueなりtwitterなりでご連絡ください（・(ｪ)・）")

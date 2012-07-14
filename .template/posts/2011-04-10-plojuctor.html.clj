; @layout post
; @title  Plojuctor > Clojure REPL上で動作するプレゼンツールを作ってみた(半分ネタ)


(img "thumbnail" "/img/post/plojuctor.png")

(p "元ネタは以下です。")

(blockquote
  "Here Comes Clojure: A Clojure Talk in Clojure"
  (link "http://bit.ly/hU4qDx"))

(p "中身が全部素のテキストだったので、こういうツールがあっても良いんじゃないかなぁ
   という軽い気持ちでツールにしてみました。")

(h2 "Plojuctor")

(link "https://github.com/liquidz/plojuctor")

(p "テストコードもドキュメントも書いてない&alpha;バージョンです。
   動作がおかしいところもあると思いますがご了承ください。")

(h3 "使い方")

#-SH
$ git clone git://github.com/liquidz/plojuctor.git
$ cd plojuctor
$ lein deps
$ lein repl
user=> (use 'plojuctor.core :reload)
user=> (next-page!)
SH

(p "スライドの移動は " (code (next-page!)) " , " (code (prev-page!)) " で行います。"
   (code (move-page! N)) " でページ番号指定の移動も可です。")

(h3 "VimClojure")

(p "元ネタ同様に単純なClojure REPL上では色付けされません。
   VimClojure上のREPLで動作させるとカラースキームによって色付けされます。")

(h3 "プレゼンの書き方")

(p "詳細は plojuctor.core, plojuctor.sentence を参照してください。
   気が向いたらドキュメント書きます。
   以下は単純な例です。")

#-CLJ
(defslide
 (title "スライドタイトル")
 (item "itemは箇条書き"
  (code (println "コードはこんな感じ"))
  "スライドは定義した順に表示"))

(defslide
 (middle-page
  (center-page
   "上下左右中央")))
CLJ

(h2 "最後に")

(p "基本的なセンテンスは実装したつもりなので、あとは関数なりマクロなりで
   自分の使いやすいようにカスタマイズすれば良いかなぁという感じです。
   ただ実際のプロジェクタで表示した時に見やすいかは全く保証できませんが、、、")


(p "というClojureネタでした。")



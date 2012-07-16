; @layout post
; @title  Clojureでブログ生成: misakiのベータ版公開

(p "実際の公開からちょっと間空いてしまいましたが、
   [misaki](https://github.com/liquidz/misaki)のベータ版を公開しました。")

(h2 "misakiとは?")
(p "rubyの[Jekyll](https://github.com/mojombo/jekyll)を参考にClojureで作った静的サイトジェネレータです。当ブログもmisakiで生成していて、
   misaki自体の[ドキュメント](http://liquidz.github.com/misaki/)もmisakiで管理・生成しています。")

(h2 "何が新しいの?")
(p "ベータ版で追加した主な機能は以下の通りです。")
(ul
  ["Clojurescriptへの対応"
   "出力のカスタマイズ"
   "ビルトイン関数の拡充"
   "スマホ対応"])

(h2 "Clojurescript対応")
(p "設定ファイルに定義を追加することでClojurescriptをビルドすることができるようになりました。
   今まではテンプレートをS式で書くだけでしたが、これでJSもS式で書けるようになったので
   また一歩、全部Clojureのターンに近づいた感じです。")

#-CLJ
 ;; clojurescript compile options
 ;; src-dir base is `:template-dir`
 ;; output-dir base is `:public-dir`
 :cljs {:src-dir       "cljs"
        :output-to     "js/hello.js"
        :optimizations :whitespace
        :pretty-print true}
CLJ

(p "上記の定義を _config.clj に書く(デフォルトでコメントアウトされています)ことで
   テンプレートディレクトリ(`:template-dir`)内の`cljs`ディレクトリにあるcljsファイルを
   出力先ディレクトリ(`:public-dir`)の`js/hello.js`としてビルドします。")


(h2 "出力のカスタマイズ")
(p "Alpha版で指定できたポストファイル名の正規表現と出力ファイル名は
   ブログ生成に重点を置いていたため日付が付くことが前提でカスタマイズとしては微妙な機能でした。

   Beta版では日付への縛りをなくすことでより自由な出力ができるようになっています。
   その良い例がmisakiの[ドキュメント](http://liquidz.github.com/misaki/)です。")

#-CLJ
 ;; post setting
 ;;   default value: #"(\d{4})[-_](\d{1,2})[-_](\d{1,2})[-_](.+)$"
 :post-filename-regexp #"(\d{4})[-_](\d{1,2})[-_](\d{1,2})[-_](.+)$"
 ;:post-filename-format "{{year}}-{{month}}/{{filename}}"
 :post-filename-format "toc/{{filename}}"

 ;; post sort type (:date :name :title :date-desc :name-desc :title-desc)
 ;;   default value: :date-desc
 :post-sort-type :name
CLJ

(p "ドキュメントの`:post-filename-regexp`はデフォルトのままですが、
   実際のポストのファイル名は" (_string "01-getting-started.html.clj")
   "となっており、日付に縛られていないことがわかります。

   なおファイル名の先頭の数字はポストのソート順(`:post-sort-type`)で使っているのみで
   この数字も必須というわけではありません。")

(h2 "ビルトイン関数の拡充")
(p "Alpha版ではhiccupを使ったS式からHTMLへのコンパイラという意味合いが強かったのですが、
   Beta版ではテンプレートを生成するためのビルトイン関数を(ある程度)充実させました。
   それにより、より容易にサイトを構成することが可能になっています。

   詳細は[ビルトイン関数のAPI](http://liquidz.github.com/misaki/api/uberdoc.html#misaki.html.core)を参照してください。")

(h2 "スマホ対応")
(p "「ビルトイン関数の拡充」にも関係してきますが、ビルトイン関数で提供している
   `header`, `container`, `footer` など共通の機能を使っている場合には
   スマホでもそこまで違和感なく表示できるようにしました。

   なお当ブログはいろいろカスタマイズしてるので、まだスマホ対応ちゃんとしてません。。そのうちします(´・ω・｀)"
   (warn "あくまで共通機能を使っている場合のみなので、
         どんな使い方をしてもスマホ対応できるわけではないのでご注意ください。
         また「違和感なく見れる程度」なのでスマホに最適化されているわけでもありません。"))


(h2 "どう使えばいいの?")
(p "[ドキュメント](http://liquidz.github.com/misaki/)、もしくは[以前のエントリー](title:ClojureでJekyllライクなブログジェネレータ「misaki」を作ったった)に具体例をまとめているのでそちらを参照してください。")

(h2 "最後に")

(p "ソースは[GitHub](https://github.com/liquidz/misaki/)で管理しているので
   動作のおかしい箇所や要望はIssuesに追加してもらえるとありがたいです！")

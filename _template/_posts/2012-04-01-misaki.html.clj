; @layout post
; @title  ClojureでJekyllライクなブログジェネレータ「misaki」を作ったった

(p "エイプリルフールに被ってしまいましたが、
   Clojureで"
   (html/link "Jekyll" "https://github.com/mojombo/jekyll")
   "ライクなブログジェネレータを作りました。")

[:p (html/link (html/img "misaki logo" "/img/post/misaki-logo.png")
           "https://github.com/liquidz/misaki")]
(p (html/link "misaki / Jekyll inspired static site generator" "https://github.com/liquidz/misaki"))



(p "当ブログはJekyllを使ってGitHub Pages上で運用しているのですが、
   " (html/code "jekyll --server")
   " のファイル変更から反映までの遅さ(何か手があるのかもしれないですが)
   とClojurianで主にClojureのブログなのにrubyで運用してるのが納得いかなかったので(rubyは好きですが)
   作ってみました。")

(ps "\"misaki\" という名前は Jekyll が人名なので同じく人の名前にしたかったのと
    \"美しく咲く\" というがちょっと綺麗かなぁという軽いのりで付けてます。"

    "あ、でも伊東美咲は好きです。綺麗なお姉さん好きです。")


(h2 "サンプルの実行")

(p "以下のコマンドでGitHubからとってこれます。")

#-SH
$ git clone git://github.com/liquidz/misaki.git
$ cd misaki
$ lein run sample
SH

(p "ローカルサーバが起動するので "
   (html/link "http://localhost:8080/")
   " にアクセスしましょう。")

(p "なおサンプルと同じものをデモページとして"
   (html/link "こちら" "http://liquidz.github.com/project/misaki/")
   "にも公開しています。")

(h2 "自分のブログを作る")

(p "1からファイルを用意しても良いのですが、面倒だと思うのでサンプルをコピーして
   ローカルサーバを起動させましょう。")

#-SH
$ cp -pir sample your_blog
$ lein run your_blog
SH

(p "ローカルサーバの起動中は "
   (html/link "watchtower" "https://github.com/ibdknox/watchtower")
   " でテンプレートファイルを監視しているので変更すれば自動的にHTMLへのコンパイルが走ります。")

(p "デフォルトではコピーしたディレクトリ配下の \"_template\" 内にテンプレート(.clj)、ポストファイル(_post)、レイアウトファイル(_layout)が
   配置されているので、そのあたりを編集しつつブラウザで確認という流れで作っていくことになります。")

#-SH
$ vi your_blog/_template/index.html.clj
SH


(p "なおテンプレートなどディレクトリ構成の詳細はドキュメントの "
   (html/link "Directory Structure" "https://github.com/liquidz/misaki/wiki/Directory-Structure")
   " を参照してください。")

(p "ただドキュメントよりサンプルソースを見たほうが早いかもしれないです。")


(h2 "テンプレートサンプル")

(p "テンプレートはclojureのコードになっています。HTMLへのコンパイルには "
   (html/link "hiccup" "https://github.com/weavejester/hiccup")
   " を使っているのでhiccupを使ったことのある方であればすんなり入れるかなと思っています。")


#-CLOJURE
;; テンプレートオプション
;; ----
;; レイアウト定義
; @layout  default
;; テンプレートタイトル
; @title   sample tempalte

;; Clojureコードなので関数定義できます
(defn h1 [s] [:h1 s])

;; site変数でテンプレートオプションにアクセスできます
(h1 (:title site))
[:p "Welcome to misaki world!"]

;; コードハイライト
#-CLJ
(println "google-code-prettify highlight!")
CLJ
CLOJURE

(p "テンプレートではコメント内に以下のフォーマットでテンプレートオプションを定義できます。")

#-CLJ
; @key value
CLJ

(ps "テンプレートオプションではそのテンプレートに適用するレイアウトを指定したり、
    テンプレート自体のタイトルを定義することができます。"

    "レイアウトはテンプレート同様にClojureコードなので
    オプションや関数を同様に定義することができます。
    共通して使う定義や関数についてはレイアウト側で定義しておくと便利かもしれないです。")

(p "レイアウトの詳細についてはドキュメントの "
   (html/link "Edit Template" "https://github.com/liquidz/misaki/wiki/Edit-Template")
   " の \"Layout file\" の項目を参照してください。")

(p "なおテンプレートオプションへのアクセスについては
   上記サンプルにもある通り " (html/code site) " といった特別な変数が利用できます。")

(h3 "site の内容")

#-CLJ
site
;=> {:title "sample template"
;    :date  org.joda.time.DateTime  ;更新日
;    :posts [post1 post2 ... postN]
CLJ

(p (html/code :posts) "の内容については後述します。")

(h2 "エントリーの追加")

(ps "デフォルトでは _template/_posts/ 配下にファイルを作成することで
    ブログのエントリーを追加することができます。"

    "エントリーファイルはJekyll同様にファイル名のフォーマットが以下のように決まっています。")

(p [:strong "YYYY-MM-DD-タイトル.html.clj"])

(p "このファイル名が " [:strong "/YYYY/MM/タイトル.html"] " というURLに対応します。")

(h3 "(:posts site) の内容")

#-CLJ
(first (:posts site))
;=> {:title "post title"
;    :url   "post url"
;    :date  org.joda.time.DateTime  ;ファイル名の日付
;    :file  java.io.File ; エントリーファイル
;    :lazy-content エントリーの内容(clojure.lang.Delay) }
CLJ


(p "なおテンプレート編集やレイアウトに関する詳細はドキュメントの "
   (html/link "Edit Template" "https://github.com/liquidz/misaki/wiki/Edit-Template")
   " を参照してください。")

(h2 "出力されたHTML")

(ps "今回の例でいえば \"your_blog/\" ディレクトリ配下がドキュメントルートになり
    コンパイルされたHTMLはすべて your_blog 配下に出力されます。"

    "なのでGitHub Pagesへ反映する場合にはこのディレクトリ毎pushしてしまえば良いのですが、
    その場合、テンプレートファイルも一緒にpushされます。"

    "これは私自信がテンプレートとHTMLを別レポジトリで管理したくないから
    という理由でこのようなディレクトリ構成にしているのですが、
    もしテンプレートとHTMLを別々に管理したい場合には _config.clj でmisaki内で扱うディレクトリのパスを扱っており、
    それを変更することで実現可能です。")


(h2 "ドキュメント")

(p "本エントリではあくまで紹介という体で全部の機能は紹介していませんし、
   紹介している機能でも詳細を省いたりしています。")

(p "少しでも興味をもっていただけたのならば、GitHubの "
   (html/link "Wiki" "https://github.com/liquidz/misaki/wiki")
   " にまとめていますのでご参照ください。")

(p "なお英文はかなり適当です。。")


(h2 "最後に")

(ps "まだアルファバージョンということでとりあえず動くかな？というレベルです。
    今後の流れとしてはまずはこのブログ自体をJekyllからmisakiに移行させて
    足りない機能などあれば追加していきたいと思っています。"

    "またClojureScriptにも対応させてJSを置き換えたいですし、
    CSSまわりで良いライブラリがあればそれを導入して、
    前回のエントリのタイトルじゃないですが、全部Clojureでブログ運用
    とか出来ても良いのかなぁと思っています。")


(h3 "参考ページ")
(html/links
  "Jekyll" "https://github.com/mojombo/jekyll"
  "Simple static blog with Clojure" "http://thegeez.github.com/2012/03/15/static_blog_on_github_with_enlive.html"
  "Clojureのリードマクロでヒアドキュメント実装してみた" "http://d.hatena.ne.jp/nokturnalmortum/20100527/1274961805")

; @layout post
; @title  ClojureでJekyllライクなブログジェネレータ「misaki」を作ったった

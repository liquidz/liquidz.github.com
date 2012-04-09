; @layout post
; @title  自分用に heroku + clojure + mongodb のテンプレートプロジェクト作った

[:strong "追記: このテンプレートは以下を参考にしています。"]
(html/quote
  "Clojure on Heroku with Noir and Mongo in 10 minutes"
  (html/link "http://thecomputersarewinning.com/post/clojure-heroku-noir-mongo"))

(p "heroku + clojure + mongo の環境作りに
   ちょっと時間を使ってしまったのでテンプレートプロジェクトとしてまとめてみました。自分用に。")

(html/link "https://github.com/liquidz/heroku-template")

(h2 "準備")

(p "leiningen, git, heroku はインストールしておく")

(h2 "使い方(nsがmytestの場合)")

#-SH
$ git clone git://github.com/liquidz/heroku-template.git mytest
$ cd mytest
$ ./setup.sh mytest
$ heroku create --stack cedar
$ heroku addons:add mongohq:free
$ git push heroku master
SH

(p "setup.sh は各ファイルの ns を変換するスクリプト")

(p "なおローカルで実行する場合には以下で実行すると、reload, stacktrace が有効になります")

#-SH
$ lein run -m mytest.dev
SH


(p "あとは煮るなり焼くなり好きにする感じで")


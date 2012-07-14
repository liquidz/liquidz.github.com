; @layout post
; @title  DotCloudでCompojureしてみた(Hello World, MySQL)

(p "約2ヶ月の時間を経てDotCloudのinvitationがようやくきたからいじってみました。")

(p (link "@making" "http://twitter.com/making")
   "さんの以下の記事と同じことをClojureでやってみただけなので
   真新しいことは何一つやってませんのでご承知置きください。")

(blockquote
  (link "新しいPaaS？のDotCloudを試す" "http://blog.ik.am/entry/view/id/59/title/%E6%96%B0%E3%81%97%E3%81%84PaaS%EF%BC%9F%E3%81%AEDotCloud%E3%82%92%E8%A9%A6%E3%81%99/"))

(p "なおeasy_install, leiningenはインストール済みという前提で進めます。")

(h2 "環境作り")
(p "以下の公式ドキュメントに沿って進めます。")

(link "http://docs.dotcloud.com/static/tutorials/firststeps/")

(p "dotcloudのインストール")

#-SH
$ sudo easy_install dotcloud
SH

(p "アプリケーションの作成")

#-SH
$ dotcloue create liquidz
SH

(p "サービスをデプロイ")

#-SH
$ dotcloud deply -t java liquidz.www
SH

(p "今回はClojureを使うのでサービスのタイプはJavaを選びました。
   またサービス名を liquidz.www としてので、このサービスには"
   (link "http://www.liquidz.dotcloud.com/")
   "というURLが割り当てられることになります。")


(h2 "サービスの作成")
(p "Compojureを使ってサクッと作ります。")

#-SH
$ lein new liquidz
$ cd liquidz
SH

(h3 "project.clj")
(p "こちらも" (link "@making" "http://twitter.com/making") "さんの以下の記事をベースにwarファイルを作成できるようにしています。"
(link "@making" "http://twitter.com/making") "さん様々です。")
(link "http://blog.ik.am/entry/view/id/58/title/Clojureで作成したWebアプリをAWS Elastic Beanstalkにデプロイ/"
           "http://blog.ik.am/entry/view/id/58/title/Clojure%E3%81%A7%E4%BD%9C%E6%88%90%E3%81%97%E3%81%9FWeb%E3%82%A2%E3%83%97%E3%83%AA%E3%82%92AWS+Elastic+Beanstalk%E3%81%AB%E3%83%87%E3%83%97%E3%83%AD%E3%82%A4/")

#-CLJ
(defproject liquidz "1.0.0-SNAPSHOT"
  :description "FIXME: write"
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [compojure "0.5.3"]]
  :dev-dependencies [[lein-ring "0.2.4"]]
  :uberjar-name "service/root.war"
  :ring {:handler liquidz.core/app})
CLJ

(p "uberjar-nameがなぜ" (_string "service")
   "配下なのかは後ほど説明します。"
   "また war のファイル名は root.war にすると http://www.hogehoge.dotcloud.com/ といった
   " (_string "/") "直下でサービスで動きます。")

(h3 "core.clj")
(p "単純なhello worldです。")

#-CLJ
(ns liquidz.core
  (:use [compojure core route]))

(defroutes app
  (GET "/" _ "hello clojure world")
  (not-found "page not found"))
CLJ

(h3 "動作確認&war化")

(p "ローカルでring起動")

#-SH
$ lein ring server
SH

(p "war化")

#-SH
$ mkdir service
$ lein ring uberwar
SH

(p "war化する際に存在しないディレクトリ配下にwarを吐こうとするとエラーになるので
   事前にディレクトリは作成しておきます。")

(h2 "DotCloudへのプッシュ")
(p "プッシュも公式ドキュメントに書いてある通り行います。
   なお以下コマンドはカレントディレクトリがleiningenで作成したプロジェクト直下である想定です。")

#-SH
$ dotcloud push liquidz.www ./service
SH

(p "上記コマンドで service 配下にある全てのファイルがDotCloud上にrsyncされます。
   warファイルの吐き出し先を service にしたのは、ソースファイルなどまで rsync されるのが気持ち悪かったためです。")

(p "プッシュが完了すると以下のURLから動作が確認できます。")

(link "http://www.liquidz.dotcloud.com/")

(p "ね？簡単でしょ？")

(h2 "MySQLも試す")
(p "元記事と同様にMySQLも試します。MySQLサービスの作成手順はまったく同じです。")

#-SH
$ dotcloud deploy -t mysql liquidz.db
$ dotcloud info liquidz.db
SH

(p "上記infoで作成したMySQLのrootのパスワード、ホスト名などなどがわかります。")

(h3 "サービスの修正")
(p "MySQLへアクセスするようちょっと修正します。
   今回はMySQLアクセス用に /sql というルートを追加しました。")

#-CLJ
(ns liquidz.core
  (:use
     [compojure core route]
     [clojure.contrib.sql]))

(def db {:classname "com.mysql.jdbc.Driver"
         :subprotocol "mysql"
         :subname "//db.liquidz.dotcloud.com:1455"
         :user "root"
         :password "パスワード" })

(defroutes app
  (GET "/" _ "hello clojure world")
  (GET "/sql" _ (with-connection db
                  (with-query-results rs ["select 40+2"]
                    (-> rs first str))))
  (not-found "page not found"))
CLJ

(h3 "MySQLのJDBCドライバを準備")
(p "以下から拾ってきて leiningen で作成したプロジェクトの lib にコピーしておきます。"
   (link "http://java.keicode.com/lib/mysql-driver.php"))

(p "これがないと " (_string "java.sql.SQLException: No suitable driver") " のようなエラーが出るはずです。")

(h3 "動作確認&war化&プッシュ")
(p "前回同様です。")

(p "ローカルでring起動")

#-SH
$ lein ring server
SH

(p "war化")

#-SH
$ lein ring uberwar
SH

(p "push")

#-SH
$ dotcloud push liquidz.www ./service
SH

(p "プッシュが完了したら本番環境でも確認してみましょう。")

(link "http://www.liquidz.dotcloud.com/sql")

(p (code {:40+2 42})
   "こんな結果が返ってくるはずです。
   ちゃんとMySQL上で足し算ができ、結果も受け取れてますね。")

(h2 "感想")
(p "想像以上に簡単に動いたのでビックリしました。
   言語もPHP, Python, Ruby, Javaと使えるので、何か作って公開したいという時にはかなり有力候補になるんじゃないかなぁと。")

(p "ただ、まだじっくり触れてない何とも言えないですが、現状はデプロイ、プッシュが簡単に出来るコマンド類と実際にアプリが動く環境が提供されているだけなので、
   テストする際には自前でいろいろ準備しないといけないはず。
   その辺りは優秀なテスティングフレームワークがデフォルトで提供されているappengineに軍配が上がる感じです。")

(p "でもappengineと比べると")

(ul ["SQLが使える"
          "SpinUp Timeがない"])

(p "という2点だけでも大きな利点になるではないかなぁと思いました。")

(p "そんな感じで開発はものすごくしやすい環境ではあるので、
   まだベータに申し込んでない人は申し込んでみるのをオススメします。")

(link "http://www.dotcloud.com/")




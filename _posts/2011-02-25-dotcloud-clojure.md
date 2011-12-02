---
layout: post
title: DotCloudでCompojureしてみた(Hello World, MySQL)
---

# {{page.title}}
<p class="meta">2011-02-25</p>


約2ヶ月の時間を経てDotCloudのinvitationがようやくきたからいじってみました。

[@making](http://twitter.com/making)さんの以下の記事と同じことをClojureでやってみただけなので
真新しいことは何一つやってませんのでご承知置きください。

> [新しいPaaS？のDotCloudを試す](http://blog.ik.am/entry/view/id/59/title/%E6%96%B0%E3%81%97%E3%81%84PaaS%EF%BC%9F%E3%81%AEDotCloud%E3%82%92%E8%A9%A6%E3%81%99/)

なおeasy_install, leiningenはインストール済みという前提で進めます。

## 環境作り
以下の公式ドキュメントに沿って進めます。

[http://docs.dotcloud.com/static/tutorials/firststeps/](http://docs.dotcloud.com/static/tutorials/firststeps/)

dotcloudのインストール

{% highlight sh %}
$ sudo easy_install dotcloud
{% endhighlight %}

アプリケーションの作成

{% highlight sh %}
$ dotcloue create liquidz
{% endhighlight %}

サービスをデプロイ

{% highlight sh %}
$ dotcloud deply -t java liquidz.www
{% endhighlight %}

今回はClojureを使うのでサービスのタイプはJavaを選びました。
またサービス名を liquidz.www としてので、このサービスには
[http://www.liquidz.dotcloud.com/](http://www.liquidz.dotcloud.com/)というURLが割り当てられることになります。


## サービスの作成
Compojureを使ってサクッと作ります。

{% highlight sh %}
$ lein new liquidz
$ cd liquidz
{% endhighlight %}

### project.clj
こちらも[@making](http://twitter.com/making)さんの以下の記事をベースにwarファイルを作成できるようにしています。
[@making](http://twitter.com/making)さん様々です。
http://blog.ik.am/entry/view/id/58/title/Clojure%E3%81%A7%E4%BD%9C%E6%88%90%E3%81%97%E3%81%9FWeb%E3%82%A2%E3%83%97%E3%83%AA%E3%82%92AWS+Elastic+Beanstalk%E3%81%AB%E3%83%87%E3%83%97%E3%83%AD%E3%82%A4/

{% highlight clj %}
(defproject liquidz "1.0.0-SNAPSHOT"
  :description "FIXME: write"
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [compojure "0.5.3"]]
  :dev-dependencies [[lein-ring "0.2.4"]]
  :uberjar-name "service/root.war"
  :ring {:handler liquidz.core/app})
{% endhighlight %}

uberjar-nameがなぜ "service" 配下なのかは後ほど説明します。
また war のファイル名は root.war にすると http://www.hogehoge.dotcloud.com/ といった
"/"直下でサービスで動きます。

### core.clj
単純なhello worldです。

{% highlight clj %}
(ns liquidz.core
  (:use [compojure core route]))

(defroutes app
  (GET "/" _ "hello clojure world")
  (not-found "page not found"))
{% endhighlight %}

### 動作確認&war化

ローカルでring起動

{% highlight sh %}
$ lein ring server
{% endhighlight %}

war化

{% highlight sh %}
$ mkdir service
$ lein ring uberwar
{% endhighlight %}

war化する際に存在しないディレクトリ配下にwarを吐こうとするとエラーになるので
事前にディレクトリは作成しておきます。

## DotCloudへのプッシュ
プッシュも公式ドキュメントに書いてある通り行います。
なお以下コマンドはカレントディレクトリがleiningenで作成したプロジェクト直下である想定です。

{% highlight sh %}
$ dotcloud push liquidz.www ./service
{% endhighlight %}

上記コマンドで service 配下にある全てのファイルがDotCloud上にrsyncされます。
warファイルの吐き出し先を service にしたのは、ソースファイルなどまで rsync されるのが気持ち悪かったためです。

プッシュが完了すると以下のURLから動作が確認できます。

[http://www.liquidz.dotcloud.com/](http://www.liquidz.dotcloud.com/)

ね？簡単でしょ？

## MySQLも試す
元記事と同様にMySQLも試します。MySQLサービスの作成手順はまったく同じです。

{% highlight sh %}
$ dotcloud deploy -t mysql liquidz.db
$ dotcloud info liquidz.db
{% endhighlight %}

上記infoで作成したMySQLのrootのパスワード、ホスト名などなどがわかります。

### サービスの修正
MySQLへアクセスするようちょっと修正します。
今回はMySQLアクセス用に /sql というルートを追加しました。

{% highlight clj %}
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
{% endhighlight %}

### MySQLのJDBCドライバを準備
以下から拾ってきて leiningen で作成したプロジェクトの lib にコピーしておきます。
[http://java.keicode.com/lib/mysql-driver.php](http://java.keicode.com/lib/mysql-driver.php)

これがないと "java.sql.SQLException: No suitable driver" のようなエラーが出るはずです。

### 動作確認&war化&プッシュ
前回同様です。

ローカルでring起動

{% highlight sh %}
$ lein ring server
{% endhighlight %}

war化

{% highlight sh %}
$ lein ring uberwar
{% endhighlight %}

push

{% highlight sh %}
$ dotcloud push liquidz.www ./service
{% endhighlight %}

プッシュが完了したら本番環境でも確認してみましょう。

[http://www.liquidz.dotcloud.com/sql](http://www.liquidz.dotcloud.com/sql)

"{:40+2 42}" こんな結果が返ってくるはずです。
ちゃんとMySQL上で足し算ができ、結果も受け取れてますね。


## 感想
想像以上に簡単に動いたのでビックリしました。
言語もPHP, Python, Ruby, Javaと使えるので、何か作って公開したいという時にはかなり有力候補になるんじゃないかなぁと。

ただ、まだじっくり触れてない何とも言えないですが、現状はデプロイ、プッシュが簡単に出来るコマンド類と実際にアプリが動く環境が提供されているだけなので、テストする際には自前でいろいろ準備しないといけないはず。
その辺りは優秀なテスティングフレームワークがデフォルトで提供されているappengineに軍配が上がる感じです。

でもappengineと比べると

 * SQLが使える
 * スピンアップタイムがない

という2点だけでも大きな利点になるではないかなぁと思いました。

そんな感じで開発はものすごくしやすい環境ではあるので、
まだベータに申し込んでない人は申し込んでみるのをオススメします。

[http://www.dotcloud.com/](http://www.dotcloud.com/)




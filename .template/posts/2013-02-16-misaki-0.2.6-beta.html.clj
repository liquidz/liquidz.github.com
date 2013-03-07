; @layout post
; @title  misaki 0.2.6-beta リリースしますた

(p "[misaki 0.2.6-beta](https://github.com/liquidz/misaki/tree/0.2.6-beta) をリリースしました。
   変更内容は大してないので、詳細は[こちら](http://liquidz.github.com/misaki/toc/99-change-log.html)を見ていただきたいですが
   今回は特に大きな変更点であるページネーションについて簡単に説明します。")

(p "今まではトップページに出すポスト一覧は")
(ul ["全件を表示する"
     "件数を限定して表示する"])
(p "のいずれかしか選択できませんでした。

   ページネーションに対応することで普通のブログのように
   一定のポスト数毎にトップページをページ分けさせることができます。")

(h2 "設定方法")
(p "_config.cljで以下を定義してください。 ※2013/03/07修正")
##CLJ
{
 ;; 1ページあたりのポスト数
 ;;    デフォルト値: nil (ページネーション無効)
 :posts-per-page 10

 ;; 2ページ目以降のページ分けされたトップページのファイル名フォーマット
 ;;   デフォルト値: "page{{page}}/{{filename}}"
 ;;   利用可能な変数:
 ;;     @page    : ページ番号(1..N)
 ;;     @filename: ファイル名
 ;;     @name    : 拡張子抜きのファイル名
 ;;     @ext     : 拡張子
 :page-filename-format "page{{page}}/{{filename}}"
 }
CLJ

(p "次にトップページ(index.html.clj)では以下のように記述します。")

##CLJ

; ポスト一覧は今まで通りの記述でおｋ
(post-list)

; 前のページ、次のページへのリンク作成
(prev-next-page-link)

;; リンクのデザインをカスタマイズしたい場合には
;; `site`変数からアクセスできるので以下の記述でもおｋ
(if-let [url (:prev-page site)]
  (link "前のページ" url))

(if-let [url (:next-page site)]
  (link "次のページ" url))
CLJ

(p "より詳細な内容については[ドキュメント](http://liquidz.github.com/misaki/toc/08-pagination.html)を参照していただければと思います。")

(h2 "最後に")
(p "ページネーションについてはポスト毎にページ分けされることを
   テンプレート内でほとんど意識しなくてもいいような作りにしているつもりです。

   また今回は`default`コンパイラで独自のソートがある関係でソース修正が多かったですが
   外部コンパイラでは特別な修正なく対応できるような作りにもしているので
   misakiコンパイラを開発する側にも恩恵は大きいのかなと思っています。

   バグなどあれば[uochan](http://twitter.com/uochan)までご連絡ください！")


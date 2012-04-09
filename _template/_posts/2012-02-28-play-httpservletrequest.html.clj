; @layout post
; @title  Play frameworkでHttpServletRequestを扱うメモ

(warn
 "本記事の内容は Play framework 開発者の方々意向で
 ドキュメントに載せていない隠しAPI的な位置づけの機能を使っています。

 どうしても使わざるを得ない場合を除いては使わない方が良いかと思います。")

(p "ちょっとしたものを Play framework(Java) で書いて
   Tomcat上で動かすということをしているのですが、")

(html/ul ["Apache ->(mod_proxy_ajp)-> Tomcat"])

(ps "という連携でApacheからTomcatに渡される環境変数を
    playで受け取れなくて困っていました。"

    "そこで色々調べたのですが HttpServletRequest を扱う方法がありました。")

[:p {:class "paragraph" :style "font-size: x-large;"}
 "隠されてましたが"]

(html/quote
  (html/link "#99: Lighthouse 588 patch by grandfatha for playframework/play - Pull Request - GitHub"
             "https://github.com/playframework/play/pull/99"))

(p "すべてはここに書いてあります。
   ものすごく意訳すると")

(html/quote
  "こんなに美しくないアイデアはないけど、"
  "Servlet APIにアクセスせざるを得ないユーザがいるのは事実。"
  "モジュールに切り離せればベストだけどそれもできないからマージするよ。"
  "でもこんなのドキュメントになんか書いてあげないんだからねっ！")

(p "ということでコントローラ内から以下のコードでアクセスできます。")

#-JAVA
HttpServletRequest req = (HttpServletRequest)request.args.get(ServletWrapper.SERVLET_REQ);
JAVA



(p "上記の通り、あえてドキュメントに書かれていないことなので
   2度目になりますが、どうしてもという場合以外は使わない方が良いと思います。")

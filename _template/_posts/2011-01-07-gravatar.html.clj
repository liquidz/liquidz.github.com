; @layout post
; @title  Gravatarのライブラリ作ったった

(p "需要は全然ないと思うけど自分でちょっと使いそうだったから作ったった。
   でもGravatarのAPIをそのままラッピングだけの簡単なものデス。")

(html/dl
  {:github  (html/link "https://github.com/liquidz/clj-gravatar")
   :clojars (html/link "http://clojars.org/org.clojars.liquidz/clj-gravatar")})


(p "以下、使用例")

#-CLJ
; アバター画像URL取得
(gravatar-image "your@mail.address")
; サイズ指定
(gravatar-image "your@mail.address" :size 24)
; デフォルト画像指定
(gravatar-image "your@mail.address" :default "ttp://hoge.com/fuga.png")
; HTTPS利用
(gravatar-image "your@mail.address" :secure? true)
; ------
; プロフィール取得
(gravatar-profile "your@mail.address")
; HTTPS利用
(gravatar-profile "your@mail.address" :secure? true)
CLJ

(p "デフォルト画像指定はURLちゃんと書いたらimgタグに展開されちゃったからわざと抜いて書いてます。
   あれ、でもプロフィール取得のHTTPSって必要なさそうだな。。まぁいいや。")



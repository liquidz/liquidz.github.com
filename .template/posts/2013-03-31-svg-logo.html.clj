; @layout post
; @title  ロゴのSVG対応

(p "MacBook Pro Retinaディスプレイモデルの13インチを買って「綺麗すぐる！」とはしゃいでいたのは良いのですが、今までに作ったロゴの汚さが目立ってしまったので、PNGからSVGに画像を変更する手順などメモ代わりにまとめておきます。")

(p "ひとまず最近一番いじっている[misaki](https://github.com/liquidz/misaki/)のロゴが
   Retinaディスプレイで見るとドットが目立って汚らしいのでSVGにしました。")

(p (img "/img/logo.png")
   "&nbsp;<- PNG&nbsp;|&nbsp;SVG ->&nbsp;"
   (img "/img/logo.svg"))

(p "元々ロゴは[Inkscape](http://inkscape.org/)で作っていたので変更は楽でしたが、PlainなSVGだと**44KB**とかなりファイルサイズが大きくなってしまったので、以下の手順で最適化されたSVGにしました。これで**5KB**までサイズを減らすことができます。元PNGが6KBなので1KB減っちゃいました。")

(ul ["*Save as...* から名前をつけて保存"
     "ファイル形式で *Optimized SVG* を選択して保存"
     "*Optimized SVG Output* というダイアログが出るので以下の設定で出力"])

(p (img {:style "width: 500px; height: 438px;"} "/img/post/2013-03-31/inkscape.png"))


(p "なおOptimized SVGを出力する際に[lxml]()ライブラリがインストールされていないと怒られるので、
   以下を参考にインストールしました。")

(blockquote
  "install lxml in mac 10.7"
  (link "http://blog.jianhuashao.com/2012/07/install-lxml-in-mac-107.html"))

(p "MBP Retina(OS X 10.8.3)で試した限りでは **2. brew install libxlst** は不要でした。")


(h2 "最後に")
(p "SVG使っているところってほとんど見たことがないですが、
   ロゴがRetinaディスプレイで拡大しても綺麗に見えるのはなかなか気持ちいいです。
   広まるかどうかはともかく大体のブラウザで対応しているらしいので
   使ってみるのもいいかもしれません。")


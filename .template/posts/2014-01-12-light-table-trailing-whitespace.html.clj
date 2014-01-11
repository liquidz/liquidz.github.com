; @layout post
; @title  Light Tableの簡単なプラグインを作ってみた

(p "[Light Table](http://www.lighttable.com/) 0.6.0 からオープンソースになり、さらにプラグインに対応したとのことで今、Light Table 熱がすごいですね。

    プラグインについての詳細なドキュメントはまだ出てないようですが、次々と新しいプラグインが作られているのを見て自分も負けてられないと思い、
    勉強のために既存のプラグインを参考に簡単なプラグインを作ってみました。")

(h2 "trailing-whitespace")

(ul [(link "https://github.com/liquidz/trailing-whitespace")])

(p "末尾に空白があった場合にそれを見やすく表示してくれるプラグインです。

    元々 Light Table には `:lt.objs.editor.file/remove-trailing-whitespace` という設定があり保存時に末尾空白を削除してくれますが、例え保存してもファイルを開き直さない限り末尾空白が残ってしまうので、気付かずに行ごとコピーすると末尾空白も含まれてしまいます。

    そういったことを避けるために、このプラグインを使うと保存前でも末尾空白の存在を把握できるようになります。")

(p "このプラグインを作るにあたって、Light Table は [CodeMirror](http://codemirror.net/) をベースにしているので、CodeMirror のアドオンを移植するのが一番簡単そうと思い、以下のアドオンをベースに選びました。")

(blockquote
  (link "edit/trailingspace.js" "http://codemirror.net/addon/edit/trailingspace.js")
  "Adds an option showTrailingSpace which, when enabled, adds the CSS class cm-trailingspace to stretches of whitespace at the end of lines. ")

(p "メインとなる処理はほとんど JS のソースをそのまま ClojureScript に持っていっているだけですが、
    設定の有効・無効の切り替えなど Light Table 固有の箇所は以下のプラグインを参考にしました。")

(ul [(link "Whitespace" "https://github.com/LightTable/Whitespace")])

(p "空白を可視化するというシンプルなプラグインでコードも短くわかりやすいです。
    実現したい目的と内容が近いので基本的にはこのプラグインのソースをベースにして作成しました。

    Light Table のプラグインを作ろうと思っている方はこのプラグインか次に紹介するプラグインを最初に参考すると良いと思います。")

(ul [(link "Declassifier" "https://github.com/LightTable/Declassifier")])

(p "Light Table のサンプルプラグインです。公式ドキュメントからリンクが貼られているので見ている方も多いかと思います。

    特定の文字列を置き換えるというシンプルなプラグインなので、こちらも読みやすいです。
    これもプラグイン作成の良い教材なので最初に参考にすると良いです。")

(ul [(link "claire" "https://github.com/joshuafcole/claire")])

(p "ファイル検索のプラグインです。
    0.6.0 が出てから数日で開発されたプラグインであまりの仕事の早さに驚きました。

    処理で参考にした部分はない(そこまで読めてない)のですが、プラグインの中にCSSファイルをどう取り込むかというところで
    プラグインの behaviors ファイルの書き方を参考にしました。")


(h2 "最後に")
(p "今回は既存のアドオンの移植だけなのでまだまだですが、これを機に他プラグインも研究してより便利なプラグインを作りたいなと思っています。
    そしていつかは Vimmer を卒gy ゲフンゲフン")


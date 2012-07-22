; @layout post
; @title  外部S式リード時のエラー行取得

(p "[misaki](https://github.com/liquidz/misaki)ではプロジェクト外の*.cljファイルをリードしてHTMLに変換しているのですが、
   そのcljファイルにエラーがあったとしても今まではmisaki内の評価関数内のエラーとして
   例外が発生してしまって、具体的なエラー箇所がぱっとわかる状態ではありませんでした。")


(h2 "解決策")
(p "これを解決するために`clojure.core/read`で使っている`java.io.PushbackReader`を
   プロキシする形で行数をカウントするようにしてみました。")

#-CLJ
(defn create-pushback-reader-with-line [in]
  (let [line-num (atom 1)]
    (proxy [PushbackReader IDeref] [in]
      (read [] (let [c (proxy-super read)]
                 (if (and (not= -1 c) (= \newline (char c)))
                   (swap! line-num inc))
                 c))
      (deref [] @line-num))))
CLJ

(p "readをオーバーライドして改行があった場合に`line-num`をインクリメントしているだけです。
   `clojure.lang.IDeref`を継承しているのは`deref`でカウントした行数を取得するためです。
   (`deref`を選んだのはなんとなくアクセスが楽だったので)")


#-CLJ
(require '[clojure.java.io :as io])

(let [r   (io/reader "file-path.clj")
      pbr (create-pushback-reader-with-line r)]
  ; 通常のread
  (println (.read pbr))
  ; 行数を取得
  (println @pbr))
CLJ

(p "これで各S式がファイルのどの行数に書かれているかをリード時に把握できようになったので
   あとは以下のように`clojure.core/read`のエラー時に行番号を利用するだけです。")

#-CLJ
(let [file-path "foo.clj"
      pbr       (create-pushback-reader-with-line (io/reader file-path))
      line-num  @pbr]
  (try
    (read pbr)
    (catch Exception ex
      (println "エラー箇所は" file-path "の" line-num "行目です！！"))))
CLJ

(p "上記の例は実際にはEOFまでループしなければ意味がないのですが、
   そこまで書くと肝心の部分が見づらくなるので省略しました。")

(p "なおあくまでもリード時の処理だけなので、評価時のエラー(例えば未定義変数へのアクセス)は
   評価関数内のエラーとして扱われます。")

(p "また私が他の方法を知らないのでこういった方法で対応しましたが、
   もし他にスマートな方法があれば是非教えてください！")

(h2 "最後に")

(p "misakiの[devブランチ](https://github.com/liquidz/misaki/tree/dev)には上記対応を反映済みなので、具体的にエラーにどう反映させているかなどは
   devブランチの[src/misaki/reader.clj](https://github.com/liquidz/misaki/blob/dev/src/misaki/reader.clj)をご参照ください。")


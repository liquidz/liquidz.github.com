; @layout post
; @title  Clojure1.4のReader Literalsで遊んでみた

(p "Clojure 1.4が出ました！
   前々から話題になっていたReader Literalsが使えるようになったので
   ちょっと遊んでみました。")

(h2 "まずは下準備")
(p "基本は" (link "こちら" "https://github.com/clojure/clojure/blob/master/changes.md") "に書いてある通りです。")

#-SH
$ lein new myreader
$ cd myreader
$ vi project.clj
SH

#-CLJ
(defproject myreader "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.4.0"]]) ; 1.4.0
CLJ

(p "readerの定義は src ディレクトリ直下に `data_readers.clj` というファイルを作り、そこに定義します。
   手始めに文字列を大文字に変換するreaderを定義してみましょう。")

(p "**src/data_readers.clj**")
#-CLJ
{
 upper  myreader.core/upper-case
 }
CLJ
(p "**src/myreader/core.clj**")
#-CLJ
(ns myreader.core)

(defn upper-case [s]
  `(.toUpperCase s))
CLJ

(p "ここまで書いたらreplで試してみましょう。")

#-SH
$ lein repl
SH

#-CLJ
#upper"hello, world"
;=> "HELLO, WORLD"
CLJ

(p "なおreaderの定義は *data-readers* にbindingすることでも定義できます。ただ当然のことながらbinding後のreadから有効になるので、それ以前のものには適用されません。")

#-CLJ
(defn upper-case2 [s]
  `(str "_" (.toUpperCase ~s) "_"))
;=> #'fuga.core/upper-case2
(binding [*data-readers* {'upper fuga.core/upper-case2}]
  (println #upper"hello")
  ;=> HELLO
  (println (eval (read-string "#upper\"hello\""))))
  ;=> _HELLO_
CLJ

(h2 "Gacuheのデバッグプリント")

(p "少し使えそうなものとしてGaucheのデバッグプリント`?=`を実装してみましょう。")

(p "**src/data_readers.clj**")
#-CLJ
{
 ?=   myreader.core/debug-print
 }
CLJ
(p "**src/myreader/core.clj**")
#-CLJ
(ns myreader.core)

(defn debug-print [x]
  `(let [res# ~x]
     (println "?=" res#)
     res#))
CLJ

(p "こんなものを定義してあげると以下のようなことができます。")

#-CLJ
(map inc #?=(range 10))
;?= (0 1 2 3 4 5 6 7 8 9)
;=> (1 2 3 4 5 6 7 8 9 10)
CLJ

(p "ここの値がどうなってるか確認したいけど、わざわざletで囲んでprintlnするの面倒！ということは多いと思いますがこれで解決ですね！")

(h2 "文字列中の式を展開")
(p "perlの \"$var\" 然り、rubyの \"#{var}\" 然り、他言語では文字列中で変数を展開する構文があります。これをClojureで実装してみましょう。")
(p "今回は文字列中のバッククオートに囲まれた部分を式として評価するように変換します。")

(p "**src/data_readers.clj**")
#-CLJ
{
 str myreader.core/expand-sexp
 }
CLJ
(p "**src/myreader/core.clj**")
#-CLJ
(defn expand-sexp [s]
  (let [ls (map-indexed #(if (even? %) %2 (read-string %2))
                        (str/split s #"`"))]
    `(apply str (list ~@ls))))
CLJ

(p "ではreplで試してみましょう。")

#-CLJ
(def i 100)
;=> #'hoge.core/i
#str"i = `i`"
;=> "i = 100"
#str"(+ 1 2) = `(+ 1 2)`"
;=> "(+ 1 2) = 3"
CLJ

(p "イイネ！")

(h2 "最後に")
(p "使いどころはちゃんと見極めないとですが、可能性がぐんっと広がりますね！
   そんな感じでReader Literalsで遊んでみました。")

(p "コードは一応" (link "Gist" "https://gist.github.com/2399254") "にも貼っておきます。")



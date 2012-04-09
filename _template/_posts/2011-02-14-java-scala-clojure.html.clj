; @layout post
; @title  「Java使いをScalaに引き込むサンプル集」をclojureで書いてみた

(p "元ネタは以下なので、先に参照しておくとわかりやすいと思います。")

(html/quote
  "Java使いをScalaに引き込むサンプル集"
  (html/link "http://www.mwsoft.jp/programming/scala/java_to_scala.html"))

(p "同じJVM上で動く言語としてClojureだってあるんだよというのを
   知らしめたくて書いてみました。
   なおここで示すClojureコードの例はあくまで個人的な書き方なので必ずしも正しい、効率的ではないのでおかしな点があればコメントください。
   使ってるバージョンは Clojure 1.2.0 です。")

[:hr]

(h2 "ClojureだってだいたいJavaと同じよu...ゴメンなさい嘘です")

(p "ClojureもJavaの機能をそのまま使うことができます。
   以下はFileReaderを使った例。")

#-CLJ
(import '[java.io File FileReader BufferedReader])
(let [reader (BufferedReader. (FileReader. (File. "temp.txt")))]
 (try
  (doseq [line (take-while (comp not nil?) (repeatedly #(.readLine reader)))]
   (println line))
  (finally (.close reader))))
CLJ

(p "Scalaと違ってJava使いに馴染みやすい記述とは言えないものになっています。
   ちなみに with-open マクロを使うと close が不要になります。")

#-CLJ
(with-open [reader (BufferedReader. (FileReader. (File. "temp.txt")))]
 (doseq [line (take-while (comp not nil?) (repeatedly #(.readLine reader)))]
  (println line)))
CLJ

(p "あとちょっと脱線しますが、 clojure.contrib.io/read-lines を使うともっと楽です。")

#-CLJ
(use '[clojure.contrib.io :only [read-lines]])
(doseq [line (read-lines "temp.txt")]
 (println line))
CLJ

[:hr]

(h2 "初期化での折り返しは不要ですよ")

(p "Java, Scalaは元記事のとおり。")

(h3 "Java")

#-JAVA
DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
DocumentBuilder builder = factory.newDocumentBuilder();
Document doc = builder.parse(new File("foo.xml"));
JAVA

(h3 "Scala")

#-SCALA
val factory = DocumentBuilderFactory.newInstance();
val builder = factory.newDocumentBuilder();
val doc = builder.parse(new File("foo.xml"));
SCALA

(p "Clojureだと以下です。この状況でdefを使うのは変なのでlet使ってます。")

#-CLJ
(let [factory (DocumentBuilderFactory/newInstance)
      builder (.newDocumentBuilder factory)
      doc (.parse builder (File. "foo.xml"))]
  (do something))
CLJ

(p "factory, builder を束縛する必要がないなら " (html/code ..) " を使って以下のようにも書けます。")

#-CLJ
(let [doc (.. (DocumentBuilderFactory/newInstance) (newDocumentBuilder) (parse (File. "foo.xml")))]
  (do something))
CLJ

(p "言語の説明記事じゃないので" (html/code ..) "の詳細は省きますが、Clojureなら1つ1つ束縛しなくても書けます。
なお Scala にある 別名import はClojureにはありません。(ドキュメントを見る限り)")

[:hr]

(h2 "Listや配列の初期化はClojureでは問題なし")

(p "リストの扱い易さはLisp方言であるClojureの強み。
   Common Lisp, Scheme同様に以下で出来ます。")

#-CLJ
(def ls '("abc" "def" "ghi"))
CLJ

(p "またClojureにはリストの他にベクターもあります。")

#-CLJ
(def v ["abc" "def" "ghi"])
CLJ

(p "ベクターはベクター用の操作関数を利用することで、リストよりも効率よく操作できたりします。")

[:hr]

(h2 "デフォルト引数、あるよ")

(p "元記事にある foo を参考にします。")

(h3 "Scala")

#-SCALA
def foo(c1 : Char = 'A', c2 : Char = 'B', c3 : Char = 'C') {
    println(c1.toString + c2.toString + c3.toString)
}
SCALA

(p "Clojureのcoreだけ使うと以下のようになります。")

#-CLJ
(defn foo [& {:keys [c1 c2 c3] :or {c1 "A", c2 "B", c3 "C"}}]
  (println (str c1 c2 c3)))
(foo) ; => ABC
(foo :c1 "D") ; => DBC
(foo :c2 "E" :c3 "F") ; => AEF
CLJ

(p "1つの引数の場合でも名前を指定しないといけないところがScalaとの違いです。
また " (html/code keys) " や " (html/code or) " でゴチャゴチャしてます。
ここは " (html/code clojure.contrib.def/defnk) " を使うと見やすくなります。")

#-CLJ
(use '[clojure.contrib.def :only [defnk]])
(defnk foo2 [:c1 "A" :c2 "B" :c3 "C"]
  (println (str c1 c2 c3)))
CLJ

[:hr]

(h2 "throws はClojureだって省略できるよ")

(p "こんな感じで書いても大丈夫です。Clojureも例外処理は書かなくて良いようにできています。")

#-CLJ
(defn get-connection []
  (DriverManager/getConnection "jdbc:sqlite:hoge.sqlite3"))
CLJ

[:hr]

(h2 "文字列の比較は罠になりません")

(p "元記事にあった以下の比較")

(h3 "Java")

#-JAVA
String str1 = "テスト";
String str2 = "テスト";
String str3 = new String("テスト");
System.out.println(str1 + " == " + str2 + " = " + (str1 == str2));
  //=> テスト == テスト = true
System.out.println(str1 + " == " + str3 + " = " + (str1 == str3));
  //=> テスト == テスト = false
JAVA

(p "ClojureでもScala同様に値で比較を行います。")

#-CLJ
(let [str1 "test"
      str2 "test"
      str3 (String. "test")]
  (println str1 "=" str2 "=" (= str1 str2)) ; => true
  (println str1 "=" str3 "=" (= str1 str3))) ; => true
CLJ

(p "オブジェクトの比較を行う場合は " (html/code identical?) " が使えます。")

#-CLJ
(println str1 "identical?" str2 "=" (identical? str1 str2))
(println str1 "identical?" str3 "=" (identical? str1 str3))
CLJ

[:hr]

(h2 "Clojureもいろいろ省略できます")

(p "元記事のJavaは以下。")

(h3 "Java")

#-JAVA
public int sum(int i1, int i2)  {
    return i1 + i2;
}
JAVA

(p "素直に書いても型、returnが省略されます。")

#-CLJ
(defn sum [i1 i2] (+ i1 i2))
CLJ

(p "無名関数を使うと引き数名も省略できます。")

#-CLJ
(def sum #(+ % %2))
CLJ

[:hr]

(h2 "try-catchの共通化だって")

(p "マクロ使えばできます。
   ちなみにここでいうマクロはC言語にあるようなマクロやエクセルマクロとはまったくの別物です。")

#-CLJ
(defmacro trycatch [& body] `(try ~@body (catch Exception e# nil)))
(println (trycatch (/ 10 3))) ; => 10/3 Clojureでは分数になります
(println (trycatch (/ 10 0))) ; => nil
CLJ

(p "最初の例で使った " (html/code with-open) " も同様に close するという処理を共通化したマクロです。")

[:hr]

(h2 "ループの入れ子も楽々")

(p "元記事の以下のループ")

[:h4 "Java"]

#-JAVA
for (int i = 1; i > 10; i++)
    for (int j = 1; j > 10; j++)
        System.out.println(i * j);
JAVA

(p "Scalaのforみたく、Clojureのforでもできます。")

#-CLJ
(doseq [x (for [i (range 1 10), j (range 1 10)] (* i j))]
  (println x))
CLJ

(p "ただClojureのforは他言語のforと違ってリストを返す関数なので
   " (html/code doseq) " でループして出力してあげてます。
   またScala同様に入れ子はいくつでも可能です。")

#-CLJ
(doseq [x (for [i (range 1 4), j (range 1 4), k (range 1 4), l (range 1 4)] (* i j k l))]
  (println x))
CLJ

(p "またfor内での条件も同様")

#-CLJ
(doseq [x (for [i (range 1 10), j (range 1 10) :when (or (even? i) (even? j))] (* i j))]
  (println x))
CLJ

(p "リストへの変換はもともとリストを返してるのでもちろんできます。")

[:hr]

(h2 "リスト操作なら任せろ")

(p "ClojureはLisp方言の1つなのでリスト操作はもちろん得意")

#-CLJ
(let [ls '(1 2 3 5 3 5 7 8)]
  ; ユニーク
  (println (distinct ls))
  ; 偶数奇数でのグループ分け
  (println (group-by odd? ls))
  ; 和
  (println (apply + ls))
  ; 積
  (println (apply * ls))
  ; それぞれ2倍したリスト
  (println (map #(* % 2) ls)))
CLJ

(p "2つのリストの差分は core で上手い方法が見つからなかった。。
   とりあえずセットを使うと以下のように書けます。")

#-CLJ
(use '[clojure.set :only [difference]])
(println (difference (hash-set 1 2 3 4 5) (hash-set 1 4 5)))
CLJ

(p "またClojureでもScala同様にArrayListなどは扱うことができます。")

#-CLJ
(import '[java.util ArrayList])
(let [ls (ArrayList.)]
  (doto ls (.add 1) (.add 2) (.add 3))
  (println (apply + ls))
  (println (map #(str "x=" %) ls)))
CLJ

[:hr]

(h2 "あとがき")
(p "ぜんぜんたいしたコードではないですが、ClojureでもScala並に短く書けることはわかってもらえたと思います。
   見た目のわかりやすさといった点では括弧が大きな壁にはなると思いますが、
   まぁこんな言語もあるんだよというのが伝われば幸いかなぁと思います。")



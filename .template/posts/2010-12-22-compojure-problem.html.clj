; @layout post
; @title  Compojure 0.5.3 on GAE/Jの問題点?自分だけ?

(p "ローカルでdevサーバを起動してアクセスすると
   以下の例外が起きるのはうちだけの問題？？")

#-CLJ
java.lang.NoClassDefFoundError: compojure/response/Renderable
CLJ

(p "ググっても特にそれらしい情報がなかった。
   Renderableだから0.5系全般でそうだったのかな？")

(p "で、例外からして単純に " (code defprotocol) " で生成されるはずの
   クラスファイルが参照できてないだけっぽいから、
   compojure の project.clj に以下を追加してみて問題なくなったことを確認。")

#-CLJ
:aot [compojure.response]
CLJ

(p "ちなみに上記を追加して lein compile とかすると classes に
   Renderable.class が生成されてることがわかるはず。")

(p "毎回修正しなくても良いように :aot を追加修正しただけのを
   clojarsにプッシュして作業終了。")

(link "http://clojars.org/org.clojars.liquidz/compojure")

(p "という自分のためのメモ")


; @layout post
; @title  clojureでspymemcachedの薄すぎるラッパー書いた

(p "herokuでmemcached使うのにちょうど良いライブラリが見当たらなかったので、"
   (link "spymemcached" "http://code.google.com/p/spymemcached/")
   " の set/get のみをラップしたclojureライブラリ書きました。薄い！")


(dl
  {:github  (link "https://github.com/liquidz/clj-spymemcached")
   :clojars (link "https://clojars.org/clj-spymemcached")})


(h2 "使い方")

#-CLJ
; 接続
(memcached! :host "localhost" :port 11211)
; 文字列
(cache-set :hello "world") ; default expiration = 3600
(cache-get :hello :default "default value")
; リスト
(cache-set :ls '(1 2 3 4) :expiration 3600)
(= 1 (first (cache-get :ls)))
; マップ
(cache-set :map {:a 1 :b 2})
(= 2 (:b (cache-get :map)))
CLJ

(p "キーにキーワードを使えるようにしたくらいで本当ペラッペラ。
   まあ自分用ということで。")


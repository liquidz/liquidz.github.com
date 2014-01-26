; @layout post
; @title  Light Table でコマンド実行時にユーザーからの入力を受け付ける方法(サイドバー編)

(p "個人用メモ。")

(p "本当はボトムバーでのユーザ入力をさせたいのだけれども、方法がまだわからない。。
    ひとまず Goto line の処理を参考にサイドバーでのユーザ入力方法がわかったのでそのまとめ。")


##CLJ

(ns lt.plugins.foo
  (:require
    [lt.object               :as object]
    [lt.objs.command         :as cmd]
    [lt.objs.sidebar.command :as scmd])
  (:require-macros
    [lt.macros :refer [behavior defui]]))

;; 入力を受け付けるオプションの定義
(def test-input
  (scmd/options-input {:placeholder "This is test"}))

;; 入力欄で Enter を押した時(select時)の動作を定義
(behavior ::exec-active!
          :triggers #{:select}
          :reaction (fn [this test-str]
                      (scmd/exec-active! test-str)))

;; select時の動作を入力オプションに追加
(object/add-behavior! test-input ::exec-active!)

;; 入力オプションを利用したコマンドを定義
(cmd/command {:command :test-command
              :desc    "Test: Input test"
              :options test-input
              :exec    (fn [test-str]
                         (println "test input:" test-str))})
CLJ

(h2 "参考")
(ul [(link "https://github.com/LightTable/LightTable/blob/c147f1ef45e4f16488902936198937f010864f28/src/lt/objs/find.cljs#L200")])


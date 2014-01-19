; @layout post
; @title  Light Tableプラグインでのnode.jsモジュールの使い方メモ

(p "個人用メモ。今回は [moment.js](http://momentjs.com/) を使う方法。")
[:p [:strong "2014/01/20: node.jsのpath.joinではなくlt.objs.files/joinを使うよう修正"]]

(h2 "package.json の用意")

(p "project.clj, plugin.json と同じディレクトリに作成する")

##SH
npm init
vim package.json
SH

(p "moment.js を dependencies に追加")

##JSON
"dependencies": {
    "moment": "~2.5.0"
}
JSON

(p "moment.js をインストール")

##SH
npm install
SH

(h2 "プラグイン内で moment.js を利用する")

##CLJ
(ns lt.plugins.foo
  (:require
    [clojure.string :as string]
    [lt.objs.files :as files]
    [lt.objs.plugins :as plugins]))

;; プラグイン名。あとで使う
(def PLUGIN_NAME "foo")

;; ユーザプラグインのディレクトリは lt.objs.plugins/user_plugins_dir から取得
;; TODO: PLUGIN_NAME を使わないようにしたい。。
(def moment
  (js/require
    (files/join plugins/user_plugins_dir PLUGIN_NAME "node_modules" "moment")))

(defn foo-task
  []
  (println (.format (moment) "YYYY-MM-DD HH:mm:ss")))
CLJ


(h2 "課題")
(p "ソース内のコメントにも書いた通り、ユーザプラグインのディレクトリまでは取れるけれども、作っているプラグインのディレクトリまでは取得できないため、プラグイン名を定数としてもたざるを得ず気持ち悪いこと。良い解決策を知っている方がいれば教えていただけると助かります！")

(h2 "参考")
(ul ["[Claire](https://github.com/joshuafcole/claire)"])


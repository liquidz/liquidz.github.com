; @layout  default
; @title   post default title

(def h2 (partial _header :h2))
(def h3 (partial _header :h3))
(def h4 (partial _header :h4))

(defn p [& x] [:p {:class "paragraph"} x])
(defn ps [& xs] (map p xs))
(defn warn [x] [:p {:class "paragraph add"} x])

[:article
 ; page header
 [:div {:class "page-header"}
  [:h1 [:span (-> site :title first)]
   (-> site :title rest)]
  [:p (-> site :date conv/date->string)]]

 ; contents
 [:div {:class "post"} contents]

 (html/tweet-button :lang "ja" :label "ツイート")

 [:p {:class "gotop"} (html/link "&raquo; Go page top" "#top")] ]


(html/js "http://embedtweet.com/javascripts/embed_v2.js")

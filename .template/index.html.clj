; @layout  default

(def mylinks
  (partition 2
    ["Twitter" "http://twitter.com/uochan"
     "Facebook" "http://www.facebook.com/masashi.iizuka"
     "GitHub" "https://github.com/liquidz"
     "coderwall" "http://coderwall.com/liquidz"
     "Clojars" "http://clojars.org/users/liquidz"
     "Google Play" "https://play.google.com/store/apps/developer?id=uochan"]))

(defn make-post-link [post & {:keys [first? last?], :or {first? false, last? false}}]
  [:li (if first? {:class "first"})
   (_map (if first? "[{" "{"))
   [:span {:class "date"} (_string (date->string (:date post)))]
   _space
   (link (_string (:title post)) (:url post))
   (_map (if last? "}]" "}"))])

;; POSTS

[:div {:id "posts"}
 [:ul
  (make-post-link (-> site :posts first) :first? true)
  (map make-post-link (-> site :posts rest drop-last))
  (make-post-link (-> site :posts last) :last? true)]]

;; PAGER
(prev-next-page-link :prev-label "前のページ" :next-label "次のページ")

;; ABOUT ME
[:div {:id "aboutme"}
 [:p (_comment ";; profile")]
 [:p (_map "{")
  (_keyword ":name") (_string "@uochan")]
 [:p {:class "indent"}
  (_keyword ":desc") (_string "プログラミングと紅茶とニコ動と読書が好き。"
                              "リラックマとダッフィーくんも好き。"
                              [:span {:class "bear"} "クマの人"]
                              "。Vimmer兼Clojurian")]
 [:p {:class "indent"}
  (_keyword ":link")
  (_map "[")
  (interpose _space (for [[title url] mylinks] (link (_string title) url)))
  (_map "]}")]]

[:div {:id "coderwall"}]


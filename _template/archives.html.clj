; @layout default
; @title  記事一覧

(defn date->str [d]
  (apply str (interpose "-" [(conv/month d)
                             (conv/day d)])))

(defn make-post-list [posts]
  (html/ul
    #(list [:span (date->str (:date %))]
           (html/link (:title %) (:url %)))
    posts))


[:article {:class "archives"}
 (let [post-group (group-by #(conv/year (:date %)) (:posts site))]
   (for [year (keys post-group)]
     (list
       (_header :h2 (str year))
       (make-post-list (get post-group year)))))]


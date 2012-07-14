; @layout default
; @title  記事一覧

(defn date->str [d]
  (apply str (interpose "-" [(month d)
                             (day d)])))

(defn make-post-list [posts]
  (ul
    #(list [:span (date->str (:date %))]
           (link (:title %) (:url %)))
    posts))


[:article {:class "archives"}
 (let [post-group (group-by #(year (:date %)) (:posts site))]
   (for [year (keys post-group)]
     (list
       (h2 (str year))
       (make-post-list (get post-group year)))))]


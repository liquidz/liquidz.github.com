; @base   http://liquidz.github.io

"<?xml version=\"1.0\" encoding=\"utf-8\"?>"
[:feed {:xmlns "http://www.w3.org/2005/Atom"}
 [:title (:site-title site)]
 [:link {:href (str (:base site) "/atom.xml"), :rel "self"}]
 [:link {:href (:base site)}]
 [:updated (date->xml-schema (:date site))]
 [:id (:base site)]
 [:author [:name "@uochan"]]

 (map
   (fn [post]
     [:entry
      [:title (:title post)]
      [:link  (str (:base site) (:url post))]
      [:updated (date->xml-schema (:date post))]
      [:id (str (:base site) (:url post))]
      [:content {:type "html"}
       (force (:lazy-content post) )]])
   (take (:atom-post-max site) (:posts site)))]



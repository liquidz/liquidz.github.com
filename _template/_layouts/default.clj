; @title   (code "liquidz.uo")
; @format  html5

(def _space "&nbsp;")
(def _dq [:span {:class "double-quote"} "\""])

(defn span [klass & body] [:span {:class klass} body])
(defn _symbol  [x]   (span "symbol" x))
(defn _keyword [x]   (span "keyword" x " "))
(defn _map     [x]   (span "map" x))
(defn _string  [& x] (span "string" _dq x _dq))
(defn _comment [& x] (span "comment" x))

[:head
 [:meta {:charset (:charset site)}]
 [:meta {:property "og:image"
         :content "http://liquidz.github.com/img/site_image.png"}]
 [:meta {:property "image_src"
         :content "http://liquidz.github.com/img/site_image.png"}]

 [:link {:href  "/atom.xml"
         :rel   "alternate"
         :title (:title site)
         :type  "application/atom-xml"}]
 [:title (:title site)]
 (absolute-css
   "http://fonts.googleapis.com/css?family=Inconsolata"
   "/css/prettify.css"
   "/css/sunburst.css"
   "/css/main.css")]

[:body {:id "top"}
 (github-ribbon "https://github.com/liquidz/")

 (container
   ;; header
   (header
     (list "(code " (_string "liquidz.uo") ")"))

   ;; contents
   [:div {:class "contents"} contents]

   ;; footer
   (footer
     (interpose
       (list _space "|" _space)
       [(link "Top" "/")
        (link "Archives" "/archives.html")
        (list (link "@uochan" "http://twitter.com/uochan") "&nbsp;2012")])

     (p (link (img "misaki" "/img/misaki_banner.png")
              "https://github.com/liquidz/misaki"))))

 ;; js
 (absolute-js
   "/js/prettify.js"
   "/js/lang-clj.js"
   "/js/jquery-1.7.1.min.js"
   "/js/jquery-contained-sticky-scroll-min.js"
   "/js/coderwall.js"
   "/js/main.js")]


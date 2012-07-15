{
 ;directory setting
 :public-dir   "./"
 :tag-out-dir  "tag/"
 :template-dir ".template/"
 :post-dir     "posts/"
 :layout-dir   "layouts/"

 :url-base     "/"
 :port 8080

 ; default site data
 :site {:charset    "utf-8"
        :site-title "(code \"liquidz.uo\")"
        :twitter    "uochan"
        :css        ["http://fonts.googleapis.com/css?family=Inconsolata"
                     "/css/prettify.css"
                     "/css/sunburst.css"
                     "/css/main.css"]
        :device-css ["/css/smartphone.css"]
        :js         ["/js/main.js"]
        :post-max   15}

 :lang "ja"

 :compile-with-post ["index.html.clj"]

 ;; tag setting
 :tag-layout "tag"

 :post-filename-regexp #"(\d{4})[-_](\d{1,2})[-_](\d{1,2})[-_](.+)$"
 :post-filename-format "{{year}}/{{month}}/{{filename}}"

 ;;   default value: :date-desc
 :post-sort-type :date-desc

 ;highlight setting
 :code-highlight {:CLJ "lang-clj"
                  :CLOJURE "lang-clj"}
 }


{
 ;directory setting
 :public-dir   "./"
 :tag-out-dir  "tag/"
 :template-dir ".template/"
 :post-dir     "posts/"
 :layout-dir   "layouts/"

 :url-base     "/"
 ;:url-base     "/host/0B9D8N5xmx-U-UlNVU1VRamVCTzg/"
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
        :atom-post-max 15}

 :lang "ja"

 :compile-with-post ["index.html.clj"]

 ;; tag setting
 :tag-layout "tag"

 :post-filename-regexp #"(\d{4})[-_](\d{1,2})[-_](\d{1,2})[-_](.+)$"
 :post-filename-format "{{year}}/{{month}}/{{filename}}"

 ;;   default value: :date-desc
 :post-sort-type :date-desc

 :posts-per-page 15

 ;highlight setting
 :code-highlight {:CLJ "lang-clj"
                  :CLOJURE "lang-clj"}

 :detailed-log true
 }


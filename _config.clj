{
 ;directory setting
 :public-dir   "./"
 :template-dir "_template/"
 :post-dir     "_posts/"
 :layout-dir   "_layouts/"

 ; default site data
 :site {:site-title "(code \"liquidz.uo\")"
        :post-max 15}

 :lang "ja"

 :compile-with-post ["index.html.clj"]

 ;highlight setting
 :code-highlight {:CLJ "lang-clj"
                  :CLOJURE "lang-clj"}
 }


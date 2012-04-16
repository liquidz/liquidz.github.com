; @layout  default
; @title   post default title

(def h2 (partial _header :h2))
(def h3 (partial _header :h3))
(def h4 (partial _header :h4))

;(defn p [& x] [:p {:class "paragraph"} x])
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

 (tweet-button :lang "ja" :label "ツイート")

 [:p {:class "gotop"} (link "&raquo; Go page top" "#top")] ]


"
<div id='disqus_thread'></div>
<script type='text/javascript'>
    /* * * CONFIGURATION VARIABLES: EDIT BEFORE PASTING INTO YOUR WEBPAGE * * */
    var disqus_shortname = 'codeliquidzuo';

    /* * * DON'T EDIT BELOW THIS LINE * * */
    (function() {
        var dsq = document.createElement('script'); dsq.type = 'text/javascript'; dsq.async = true;
        dsq.src = 'http://' + disqus_shortname + '.disqus.com/embed.js';
        (document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq);
    })();
</script>
<noscript>Please enable JavaScript to view the <a href='http://disqus.com/?ref_noscript'>comments powered by Disqus.</a></noscript>
<a href='http://disqus.com' class='dsq-brlink'>blog comments powered by <span class='logo-disqus'>Disqus</span></a>
"

(js "http://embedtweet.com/javascripts/embed_v2.js")


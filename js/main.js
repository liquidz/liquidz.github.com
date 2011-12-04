(function(window, undefined){

    // =makeTableOfContents {{{
    var makeTableOfContents = function(opt){
        var ulId = opt.id || "toc"
          , prefix = opt.prefix || "_h2"
          , header = $(opt.header || "h2");

        if(opt.min === undefined || (opt.min && header.length >= opt.min)){
            var i   = 0
              , ul  = $("<ul>").attr("id", ulId);

            ul.append("<li>目次</li>");
            ul.append("<li><a href='#top'>&raquo; Top</a></li>");

            header.each(function(){
                var id = prefix + (++i)
                  , h2 = $(this).attr("id", id);

                $("<li>").append(
                    $("<a>").attr("href", "#" + id).html("&raquo; " + h2.text())).appendTo(ul);
            });

            return ul;
        }

        return $();
    }; // }}}

    // =applySmoothScroll {{{
    var applySmoothScroll = function(){
        $("a[href^=#]").on("click", function(){
            var hash = this.hash;
            if(!hash || hash === "#"){ return true; }

            $("html,body").animate({scrollTop: $(hash).offset().top}, 1000, "swing");

            return false;
        });
    }; // }}}

    $(function(){
        // 目次の作成
        makeTableOfContents({ header: "#post h2", min: 3 })
            //.insertAfter($("#post p.meta"));
            .insertAfter($("#header"));

        $("#toc").containedStickyScroll({
            unstick: false
        });


//.containedStickyScroll();


        applySmoothScroll();
    });

}(this));


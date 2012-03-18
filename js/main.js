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
                    $("<a>").attr("href", "#" + id).html("&raquo; " + i + ". " + h2.text())).appendTo(ul);
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

    /* =wink {{{
    var changeBackgroundUrl = function(url){
        $("body").css("background-image", "url(" + url + ")");
    };

    var wink = function(){
        changeBackgroundUrl("/img/back2.png");
        setTimeout(function(){
            changeBackgroundUrl("/img/back.png");
        }, 100)
    };

    var getRandomSec = function(){
        return Math.floor(Math.random() * 10) * 1000;
    };

    var setWinkTimeout = function(sec){
        var timeoutSec = sec || getRandomSec();

        setTimeout(function(){
            wink();
            setWinkTimeout(getRandomSec());
        }, timeoutSec);
    };
    }}} */

    $(function(){
       // 目次の作成
       makeTableOfContents({ header: "#post h2", min: 3 })
           .insertAfter($("#header"));
       if($("#toc").length !== 0){
           $("#toc").containedStickyScroll({
               unstick: false
           });
       }

       applySmoothScroll();
    });

}(this));


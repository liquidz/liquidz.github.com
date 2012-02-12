(function(){
    var appendCoderwallBadge = function(){
        var coderwallJSONurl ="http://www.coderwall.com/liquidz.json?callback=?"
          , size = 75
          ;

        $.getJSON(coderwallJSONurl, function(data) {
            $.each(data.data.badges, function(i, item){
                var a = $("<a>")
                    .attr("href", "http://www.coderwall.com/liquidz/")
                    .attr("target", "_blank")
                    ;

                $("<img>").attr("src", item.badge)
                    .attr("float", "left")
                    .attr("title", item.name + ": " + item.description)
                    .attr("alt", item.name)
                    .attr("height", size)
                    .attr("width", size)
                    .hover(
                        function(){ $(this).css("opacity", "0.6"); }
                      , function(){ $(this).css("opacity", "1.0"); }
                    )
//                    .click( function(){ window.location = "http://www.coderwall.com/liquidz/"; })
                    .appendTo(a)
                    ;
                $("#coderwall").append(a);
            });
        });
    };

    $(function(){
       appendCoderwallBadge();
    });

}());

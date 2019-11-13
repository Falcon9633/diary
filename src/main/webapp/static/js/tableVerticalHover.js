$("table").delegate('td','mouseover mouseleave', function(e) {
    if (e.type == 'mouseover') {
        $(this).parent().addClass("vertical_hover");
        $("colgroup").eq($(this).index()).addClass("vertical_hover");
    }
    else {
        $(this).parent().removeClass("vertical_hover");
        $("colgroup").eq($(this).index()).removeClass("vertical_hover");
    }
});
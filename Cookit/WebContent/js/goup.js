document.getElementById("go-up").addEventListener("click", function(){

    document.body.scrollTop = 0;
    document.documentElement.scrollTop = 0;

});

window.onscroll = function(){ scroll() };
function scroll() {

    if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
        document.getElementById("go-up").style.display = "flex";
    } else {
        document.getElementById("go-up").style.display = "none";
    }

}
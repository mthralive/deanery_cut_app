function init() {
    var amount = document.getElementById("elemAmount").innerHTML;
    var curDate =  new Date(document.getElementById("curDate").innerHTML);
    for(var i = 0; i < amount; i++){

        var date =  new Date(document.getElementById("date" + i).value);
        var btn = document.getElementById("btn" + i);

        if( curDate.getDate() > 25 &&
            curDate.getMonth() == date.getMonth() &&
            curDate.getFullYear() == date.getFullYear() ||
            curDate.getMonth() > date.getMonth() ||
            curDate.getFullYear() > date.getFullYear()){
            btn.style.display="none";
        }
    }
}

window.onload = function() {
    init()
};
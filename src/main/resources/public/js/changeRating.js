function changeRating(rate){
    var rating = document.getElementById("rate")
    var ratingV = document.getElementById("rateV")
    console.log(ratingV.value)
    if(rate != "" && ratingV==null) {
        rating.value = rate;
    }
    else{
        console.log("hihi " + ratingV)
        var btn = document.getElementById("rating" + ratingV.value)
        btn.checked = true;
    }
}
window.onload=function () {
 changeRating();
}
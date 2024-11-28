function toggleFields() {
    var selectElement = document.getElementById("options");
    var type = ""
    if(selectElement != null) {
        var selectedValue = selectElement.value;
        type = document.getElementById(selectedValue).innerHTML
    }
    else{
        type = document.getElementById("type").innerHTML
    }
    var fields = document.getElementById("electroNight");
    var fields2 = document.getElementById("electroDay");
    var normalField = document.getElementById("counterNormal");
    if (type === "Электрический") {
        fields.classList.remove("hidden");
        fields2.classList.remove("hidden");
        normalField.classList.add("hidden");
    } else {
        fields.classList.add("hidden");
        fields2.classList.add("hidden");
        normalField.classList.remove("hidden");
    }
}
window.onload = function() {
    toggleFields()
};
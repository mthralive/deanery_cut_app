
function getSchedule() {
    var dft = document.getElementById("dF")
    var df;
    if(dft != null){
        df = dft.value
    }
    var no = document.getElementById("no").value;
    console.log(no)
    var sced = document.getElementById("scheduleId").value
    var url = ""
    if(sced != null && no == ""){
        url = "?scedId="+sced;
    }
    console.log(df);
    var masters = document.getElementById("masters")
    masters.innerHTML = ""
    var ordDate = new Date(document.getElementById("ordDate").value).toISOString().split('T')[0];
    var sched = document.getElementById("schedule");
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    console.log('http://localhost:8080/api/order/schedule/' + ordDate + url)
    fetch('http://localhost:8080/api/order/schedule/' + ordDate + url, {
        method: "GET",
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-Token': csrfToken
        }
    }).then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.text();
    }).then(data => {
        console.log(sched)
        console.log(data);
        sched.style.display = "block";
        sched.innerHTML = data;
        if(df != null && sced != null && no == ""){
            sched.value = df;

        }
        getMasters();
    })
        .catch(error => {
        console.error('There was a problem with your fetch operation:', error);
    });

}
function getMasters(){
    var no = document.getElementById("no");
    var sced = document.getElementById("scheduleId").value
    if(sced != null && no.value == ""){
        url = "?scedId="+sced;
    }
    var selectElement = document.getElementById("schedule");
    var ordDate = selectElement.value;
    console.log(ordDate)
    var masters = document.getElementById("masters")
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    console.log('http://localhost:8080/api/order/master/' + ordDate + url)
    fetch('http://localhost:8080/api/order/master/' + ordDate + url, {
        method: "GET",
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-Token': csrfToken
        }
    }).then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.text();
    }).then(data => {
        console.log(masters)
        console.log(data);
        masters.style.display = "block";
        masters.innerHTML = data;
        if(sced != null && no.value == ""){
            console.log("ininin");
            masters.value = sced;
            no.value += 'nooo';
            console.log("in - " + no.value)
        }
    })
        .catch(error => {
            console.error('There was a problem with your fetch operation:', error);
        });
}

window.onload = function() {
    getSchedule()
};

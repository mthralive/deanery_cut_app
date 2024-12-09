
function getEduPlans() {
    var search = document.getElementById("findPlan").value
    var indId = document.getElementById("individual").value
    console.log(indId)
    var url = ""
    if(search != null){
        url = "?search="+search;
    }
    console.log(url);
    var workPlans = document.getElementById("workPlans")
    workPlans.innerHTML = ""
    var eduPlans = document.getElementById("eduPlans");
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    console.log('http://localhost:8080/api/education/eduPlan/' +indId + url)
    fetch('http://localhost:8080/api/education/eduPlan/' +indId + url, {
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
        console.log(eduPlans)
        console.log(data);
        eduPlans.style.display = "block";
        eduPlans.innerHTML = data;
        getWorkPlans();
    })
        .catch(error => {
            console.error('There was a problem with your fetch operation:', error);
        });

}
function getWorkPlans(){
    var selectElement = document.getElementById("eduPlans");
    var workPlans = document.getElementById("workPlans")
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    console.log('http://localhost:8080/api/education/workPlan/' + selectElement.value)
    fetch('http://localhost:8080/api/education/workPlan/' + selectElement.value, {
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
        console.log(workPlans)
        console.log(data);
        workPlans.style.display = "block";
        workPlans.innerHTML = data;
    })
        .catch(error => {
            console.error('There was a problem with your fetch operation:', error);
        });
}

window.onload = function() {
    getEduPlans()
};

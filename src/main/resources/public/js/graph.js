function init() {
    const div = document.getElementById("meaningGraph");
    const graph = document.getElementById("graph");
    const type = document.getElementById("type").innerHTML;
    const text = div.innerText;
    const list = JSON.parse(text);
    console.log(type);

    var markers1 = [];
    if(type == "Электрический"){
        var markers2 = []
        list.forEach((el) =>{
            var marker1 = [el.month, el.value]
            var marker2 = [el.month, el.value2]
            markers1.push(marker1)
            markers2.push(marker2)
        })
    }
    else{
        list.forEach((el) =>{
            var marker1 = [el.month, el.value]
            markers1.push(marker1)
        })
    }

    // create a chart
    var chart = anychart.column();


    // create an area series and set the data
    var series1 = chart.column(markers1);
    if(type === "Водоснабжение Горячий" || type === "Водоснабжение Холодный") {
        series1.name("Потребление воды")
        series1.normal().fill("#1E90FF")
        series1.normal().stroke("#1E90FF");
        series1.hovered().fill("#00BFFF")
        series1.hovered().stroke("#00BFFF");
    }
    if(type === "Газовый") {
        series1.name("Потребление газа")
        series1.normal().fill("#9932CC")
        series1.normal().stroke("#9932CC");
        series1.hovered().fill("#BA55D3")
        series1.hovered().stroke("#BA55D3");
    }

    if(type === "Электрический"){
        console.log('hihi')
        var series2 = chart.column(markers2)
        series2.normal().fill("#FF8C00")
        series2.normal().stroke("#FF8C00");
        series2.hovered().fill("#FFA500")
        series2.hovered().stroke("#FFA500");
        series2.name("Ночное потребление")

        series1.normal().fill("#FFD700")
        series1.normal().stroke("#FFD700");
        series1.hovered().fill("#FFFF00")
        series1.hovered().stroke("#FFFF00");
        series1.name("Дневное потребление")
    }


    // set the chart title
    chart.title("График потребления");
    chart.legend(true);
    // set the titles of the axes
    chart.xAxis().title("Месяц");
    chart.yAxis().title("Потребление, ед.");

    // set the container id
    chart.container("graph");

    // initiate drawing the chart
    chart.draw();
    graph.style.height = "300px";
}

init()
const content = {area : ["", "area.html"], areaSave : ["", "../../registeredAreas.json"]};

let filesGotPromise = new Promise(function(myResolve, myReject) {
    var xmlHttpRequest;
    for (let c in content) {
        xmlHttpRequest = new XMLHttpRequest();
        xmlHttpRequest.onload = function () {
            content[c][0] = this.responseText;
            if (content[c][1] == "../../registeredAreas.json"){
                myResolve();
            }
        }
        xmlHttpRequest.open("GET", content[c][1]);
        xmlHttpRequest.send();
    }
});

filesGotPromise.then(function(value) {addContent(value);});

function addContent(value) {
    var registeredAreas = JSON.parse(content.areaSave[0]);
    var areaList = document.getElementById("content");
    
    for (let i = 0; i < Object.keys(registeredAreas).length; i++) {
        var areaContent = document.createElement("div");
        areaContent.className = "area";
        areaContent.innerHTML = content.area[0];
        areaContent.getElementsByTagName("h2")[0].innerHTML = registeredAreas[i].areaInformation.areaName;
        areaContent.getElementsByClassName("information")[0].style.display = "none";
        fillAreaInformation(areaContent.getElementsByClassName("information")[0], registeredAreas[i]);
        areaList.appendChild(areaContent);
    }
    console.log(document.body);
}

function fillAreaInformation(informationDiv, currentArea) {
    var areaName = document.createElement("p");
    var worldName = document.createElement("p");
    var materialsList = document.createElement("p");
    var point1 = document.createElement("p");
    var point2 = document.createElement("p");

    areaName.innerHTML = "Nom de la zone : " + currentArea.areaInformation.areaName;
    worldName.innerHTML = "Nom du monde contenant la zone: " + currentArea.areaInformation.worldName;
    materialsList.innerHTML = "Matériau plaçables/cassables dans la zone : " + currentArea.areaInformation.materialsList;
    point1.innerHTML = "Coordonnées du point 1 : " + "(" + currentArea.areaInformation.point1.x + "," + currentArea.areaInformation.point1.y + ")";
    point2.innerHTML = "Coordonnées du point 1 : " + "(" + currentArea.areaInformation.point2.x + "," + currentArea.areaInformation.point2.y + ")";

    informationDiv.appendChild(areaName);
    informationDiv.appendChild(worldName);
    informationDiv.appendChild(materialsList);
    informationDiv.appendChild(point1);
    informationDiv.appendChild(point2);
}

function showDetails(obj) {
    var displayState = obj.nextElementSibling.style.display;
    if (displayState === "none") {
        obj.nextElementSibling.style.display = "block";
    }
    else {
        obj.nextElementSibling.style.display = "none"; 
    }
}
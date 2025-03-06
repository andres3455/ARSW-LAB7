var app = (function(){

    var author = "";
    var blueprintName = "";
    var blueprints = [];
    var currentCanvasPoints = [];
    var canvas = document.getElementById("canvas");
    var ctx = canvas.getContext("2d");
    var apiFunction = apiclient;
    var newBlueprintInProgress = false;

    function setAuthorName(author) {
        author = author;
    }


    function drawBlueprint() {
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.beginPath();
        currentCanvasPoints.forEach(function (point, index) {
            if (index === 0) {
                ctx.moveTo(point.x, point.y);
            } else {
                ctx.lineTo(point.x, point.y);
            }
        });
        ctx.stroke();
    }   

    function updateBlueprintTable() {
        $("#blueprint-table tbody").empty();
        blueprints.map(function (blueprint) {
            var newRow = "<tr><td>" + blueprint.name + "</td><td>" + blueprint.points.length  + "</td><td><button id=" + encodeURIComponent(blueprint.name) + " onclick='app.getBlueprintsByAuthorAndName(this)'>Open</button></td></tr>";
            $("#blueprint-table tbody").append(newRow);
        });
        var totalPoints = blueprints.reduce(function (accumulator, blueprint) {
            return accumulator + blueprint.points.length;
        }, 0);
        $("#total-points").text("Total user points: " + totalPoints);
    }

    function clear(){
        clearCanvas();
        $("#author-content").text("Author Name");
        $("#total-points").text("Total user points: ");
        $("#blueprint-table tbody").empty();
        $("#blueprint-name").text("Blueprint Name");
        $("#title-newData").text("");
        $("#blueprint-name-display").text("");
        $("#blueprint-info").text("");
    }

    function clearCanvas(){
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        currentCanvasPoints = [];
        blueprintName = "";
        newBlueprintInProgress = false;
    }

    function createNewBlueprint() {
        clearCanvas();
        if (author === '') {
            alert("You have not selected an author");
            return;
        }
        blueprintName = prompt("Enter the name of the new blueprint:");
        if (blueprintName !== null && blueprintName !== "") {
            newBlueprintInProgress = true;
            $("#title-newData").text("New Blueprint Data:");
            $("#blueprint-name-display").text("Blueprint Name: " + blueprintName);
            $("#blueprint-info").text("You can add points to the new plane using the Canvas. When finished, click the Save/Update button.");
            $("#blueprint-name").text(blueprintName);
        }
    }

    function initCanvas(){
        var offset  = getOffset(canvas);
        if(window.PointerEvent) {
            canvas.addEventListener("pointerdown", function(event){
                if(blueprintName != "" ){
                    var x = event.pageX - offset.left;
                    var y = event.pageY - offset.top;
                    currentCanvasPoints.push({ x: x, y: y });
                    drawBlueprint();
                }
            });
        } else {
            canvas.addEventListener("mousedown", function(event){
                if(blueprintName != ""){
                    var x = event.pageX - offset.left;
                    var y = event.pageY - offset.top;
                    currentCanvasPoints.push({ x: x, y: y });
                    drawBlueprint();
                }
            });
        }
    }

    function createNewBlueprint() {
        clearCanvas();
        if (author === '') {
            alert("You have not selected an author");
            return;
        }
        blueprintName = prompt("Enter the name of the new blueprint:");
        if (blueprintName !== null && blueprintName !== "") {
            newBlueprintInProgress = true;
            $("#title-newData").text("New Blueprint Data:");
            $("#blueprint-name-display").text("Blueprint Name: " + blueprintName);
            $("#blueprint-info").text("You can add points to the new plane using the Canvas. When finished, click the Save/Update button.");
            $("#blueprint-name").text(blueprintName);
        }
    }
    function deleteBlueprint() {
        if (blueprintName == "" || author == "" ) {
            alert("Author or Blueprint not selected!");
            return;
        }
        apiFunction.deleteBlueprint(author, blueprintName)
            .then(function () {
                return apiFunction.getBlueprintsByAuthor(author);
            })
            .then(function (newBlueprints) {
                blueprints = newBlueprints.map(function (blueprint) {
                    return { name: blueprint.name, points: blueprint.points };
                });
                clear();
                updateBlueprintTable();
                alert("Plano eliminado exitosamente.");
            })
            .catch(function (error) {
                console.error("Error al eliminar el plano:", error);
                alert("Error al eliminar el plano.");
            });
    }

    return{
        setAuthorName: setAuthorName,
        initCanvas: initCanvas,
        createNewBlueprint: createNewBlueprint,
        deleteBlueprint: deleteBlueprint
    }
})();
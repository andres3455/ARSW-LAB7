var app = (function(){

    var author = "";
    var blueprints = [];
    var canvas;
    var ctx;
    var apiFunction = apimock;

    function setAuthorName(authorName) {
        author = authorName;
    }

    function updateBlueprintTable() {
        $("#blueprint-table tbody").empty();
        blueprints.forEach(blueprint => {
            let newRow = `<tr>
                            <td>${blueprint.name}</td>
                            <td>${blueprint.points.length}</td>
                            <td><button id="${encodeURIComponent(blueprint.name)}" onclick="app.getBlueprintsByAuthorAndName(this)">Open</button></td>
                          </tr>`;
            $("#blueprint-table tbody").append(newRow);
        });
        
        let totalPoints = blueprints.reduce((acc, blueprint) => acc + blueprint.points.length, 0);
        $("#total-points").text(`Total user points: ${totalPoints}`);
    }

    function clearCanvas() {
        if (!canvas) {
            canvas = document.getElementById("canvas");
            ctx = canvas.getContext("2d");
        }
        ctx.clearRect(0, 0, canvas.width, canvas.height);
    }

    function getBlueprintsByAuthor() {
        clearCanvas();
        author = $("#author").val().trim();
        if (!author) {
            alert("Please enter an author name.");
            return;
        }
        $("#blueprint-table tbody").empty();
        $("#author-content").text(`${author}'s blueprints:`);
        
        apiFunction.getBlueprintsByAuthor(author, function (data) {
            if (data && data.length) {
                blueprints = data.map(bp => ({ name: bp.name, points: bp.points }));
                updateBlueprintTable();
            } else {
                alert("El Autor no fue encontrado.");
                blueprints = [];
                updateBlueprintTable();
            }
        });
    }

    function drawBlueprint(blueprint) {
        clearCanvas();
        if (!blueprint.points || !blueprint.points.length) {
            alert("No points found for this blueprint.");
            return;
        }
        ctx.beginPath();
        blueprint.points.forEach((point, index) => {
            index === 0 ? ctx.moveTo(point.x, point.y) : ctx.lineTo(point.x, point.y);
        });
        ctx.stroke();
        $("#blueprint-name").text(blueprint.name);
    }

    function getBlueprintsByAuthorAndName(selectedBp) {
        author = $("#author").val().trim();
        if (!author) {
            alert("Please enter an author name first.");
            return;
        }
        let blueprintName = decodeURIComponent(selectedBp.id);
        apiFunction.getBlueprintsByNameAndAuthor(author, blueprintName, function (data) {
            data ? drawBlueprint(data) : alert("El plano no fue encontrado.");
        });
    }

    return {
        getBlueprintsByAuthor,
        getBlueprintsByAuthorAndName,
        setAuthorName
    };
})();

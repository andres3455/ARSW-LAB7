var app = (function () {
    var author = "";
    var blueprintName = "";
    var blueprints = [];
    var currentCanvasPoints = [];
    var canvas = null;
    var ctx = null;
    var apiFunction = apimock;
    var newBlueprintInProgress = false;

    function setAuthorName(authorName) {
        author = authorName;
    }

    function updateBlueprintTable() {
        $("#blueprint-table tbody").empty();
        blueprints.forEach(function (blueprint) {
            var newRow = "<tr><td>" + blueprint.name + "</td><td>" + blueprint.points.length +
                "</td><td><button id='" + encodeURIComponent(blueprint.name) +
                "' onclick='app.getBlueprintsByAuthorAndName(this)'>Open</button></td></tr>";
            $("#blueprint-table tbody").append(newRow);
        });

        var totalPoints = blueprints.reduce((acc, blueprint) => acc + blueprint.points.length, 0);
        $("#total-points").text("Total user points: " + totalPoints);
    }

    function getBlueprintsByAuthor() {
        clear();
        author = $("#author").val();
        if (!author) {
            alert("Please enter an author");
            return;
        }

        apiFunction.getBlueprintsByAuthor(author, function (data) {
            if (data) {
                blueprints = data.map(bp => ({ name: bp.name, points: bp.points }));
                $("#author-content").text(author + "'s blueprints: ");
                updateBlueprintTable();
            } else {
                alert("Author not found.");
            }
        });
    }

    function drawBlueprint() {
        if (!ctx) return; // Evita errores si el contexto no está inicializado
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.beginPath();
        currentCanvasPoints.forEach((point, index) => {
            if (index === 0) {
                ctx.moveTo(point.x, point.y);
            } else {
                ctx.lineTo(point.x, point.y);
            }
        });
        ctx.stroke();
    }

    function getBlueprintsByAuthorAndName(selectedBp) {
        author = $("#author").val().trim();
        if (!author) {
            alert("Please enter an author name first.");
            return;
        }

        let blueprintName = decodeURIComponent(selectedBp.id);
        apiFunction.getBlueprintsByNameAndAuthor(author, blueprintName, function (data) {
            if (data) {
                currentCanvasPoints = data.points;
                drawBlueprint();
            } else {
                alert("Blueprint not found.");
            }
        });
    }

    function updateSaveBlueprint() {
        if (!author || !blueprintName) {
            alert("No blueprint selected.");
            return;
        }
    
        var blueprintData = {
            author: author,
            points: currentCanvasPoints,
            name: blueprintName
        };
    
        if (newBlueprintInProgress) {
            if (currentCanvasPoints.length === 0) {
                alert("The blueprint must have at least one point before saving.");
                return;
            }
    
            apiFunction.createBlueprint(blueprintData, function (updatedBlueprints) {
                blueprints = updatedBlueprints.map(function (blueprint) {
                    return { name: blueprint.name, points: blueprint.points };
                });
                clear();
                updateBlueprintTable();
                alert("Blueprint added successfully");
            });
    
            newBlueprintInProgress = false; // Marcar como guardado
        } else {
            apiFunction.updateBlueprint(author, blueprintName, blueprintData, function (updatedBlueprints) {
                blueprints = updatedBlueprints.map(function (blueprint) {
                    return { name: blueprint.name, points: blueprint.points };
                });
                updateBlueprintTable();
                alert("Blueprint updated successfully");
            });
        }
    }
    

    function initCanvas() {
        canvas = document.getElementById("canvas");
        if (!canvas) {
            console.error("Canvas element not found!");
            return;
        }
        ctx = canvas.getContext("2d");

        function addPoint(event) {
            if (blueprintName !== "") {
                var x = event.clientX - canvas.getBoundingClientRect().left;
                var y = event.clientY - canvas.getBoundingClientRect().top;
                currentCanvasPoints.push({ x, y });
                drawBlueprint();
            }
        }

        if (window.PointerEvent) {
            canvas.addEventListener("pointerdown", addPoint);
        } else {
            canvas.addEventListener("mousedown", addPoint);
            canvas.addEventListener("touchstart", function (event) {
                event.preventDefault();
                addPoint(event.touches[0]);
            });
        }
    }

    function createNewBlueprint() {
        clearCanvas();
        if (!author) {
            alert("You have not selected an author");
            return;
        }
        var newName = prompt("Enter the name of the new blueprint:");
        if (!newName) {
            alert("Blueprint name is required.");
            return;
        }
        blueprintName = newName;
        newBlueprintInProgress = true;

        $("#title-newData").text("New Blueprint Data:");
        $("#blueprint-name-display").text("Blueprint Name: " + blueprintName);
        $("#blueprint-info").text("You can add points to the new blueprint using the canvas. Click Save/Update when done.");
        $("#blueprint-name").text(blueprintName);
    }

    function deleteBlueprint() {
        if (!blueprintName || !author) {
            alert("Author or Blueprint not selected!");
            return;
        }
        apiFunction.deleteBlueprint(author, blueprintName)
            .then(() => apiFunction.getBlueprintsByAuthor(author))
            .then(newBlueprints => {
                blueprints = newBlueprints.map(bp => ({ name: bp.name, points: bp.points }));
                clear();
                updateBlueprintTable();
                alert("Blueprint successfully deleted.");
            })
            .catch(() => alert("Error deleting blueprint."));
    }

    function clear() {
        clearCanvas();
        $("#author-content").text("Author Name");
        $("#total-points").text("Total user points: ");
        $("#blueprint-table tbody").empty();
        $("#blueprint-name").text("Blueprint Name");
        $("#title-newData").text("");
        $("#blueprint-name-display").text("");
        $("#blueprint-info").text("");
    }

    function clearCanvas() {
        if (!ctx) {
            initCanvas(); // Asegura que el contexto esté inicializado antes de limpiar
        }
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        currentCanvasPoints = [];
        blueprintName = "";
        newBlueprintInProgress = false;
    }

    return {
        getBlueprintsByAuthor,
        getBlueprintsByAuthorAndName,
        setAuthorName,
        initCanvas,
        updateSaveBlueprint,
        createNewBlueprint,
        deleteBlueprint
    };
})();

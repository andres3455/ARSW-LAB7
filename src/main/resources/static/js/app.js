var app = (function() {
    var author = "";
    var blueprintName = "";
    var blueprints = [];
    var currentCanvasPoints = [];
    var canvas = null;
    var ctx = null;
    var apiFunction = apiclient;
    var newBlueprintInProgress = false;

    function setAuthorName(authorName) {
        author = authorName;
    }

    function updateBlueprintTable() {
        $("#blueprint-table tbody").empty();
        blueprints.forEach(function(blueprint) {
            var newRow = `<tr>
                <td>${blueprint.name}</td>
                <td>${blueprint.points.length}</td>
                <td><button id="${encodeURIComponent(blueprint.name)}" onclick="app.getBlueprintsByAuthorAndName(this)">Open</button></td>
            </tr>`;
            $("#blueprint-table tbody").append(newRow);
        });

        var totalPoints = blueprints.reduce((acc, blueprint) => acc + blueprint.points.length, 0);
        $("#total-points").text(`Total user points: ${totalPoints}`);
    }

    function getBlueprintsByAuthor() {
        clearCanvas();
        author = $("#author").val().trim();
        if (!author) {
            alert("Please enter an author");
            return;
        }

        apiFunction.getBlueprintsByAuthor(author)
            .then(data => {
                if (data && data.length > 0) {
                    blueprints = data.map(bp => ({ name: bp.name, points: bp.points }));
                    $("#author-content").text(`${author}'s blueprints:`);
                    updateBlueprintTable();
                } else {
                    alert("Author not found or no blueprints available.");
                }
            })
            .catch(error => {
                console.error("Error fetching blueprints:", error);
                alert("Failed to retrieve blueprints.");
            });
    }

    function drawBlueprint() {
        if (!ctx) initCanvas();
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

        blueprintName = decodeURIComponent(selectedBp.id);

        apiFunction.getBlueprintsByNameAndAuthor(author, blueprintName)
            .then(data => {
                currentCanvasPoints = data.points;
                drawBlueprint();
            })
            .catch(() => alert("Blueprint not found."));
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

            apiFunction.createBlueprint(blueprintData)
                .then(() => {
                    alert("Blueprint added successfully");
                    newBlueprintInProgress = false;
                    return apiFunction.getBlueprintsByAuthor(author);
                })
                .then(data => {
                    blueprints = data.map(bp => ({ name: bp.name, points: bp.points }));
                    updateBlueprintTable();
                })
                .catch(() => alert("Error adding blueprint."));
        } else {
            apiFunction.updateBlueprint(author, blueprintName, blueprintData)
                .then(() => {
                    alert("Blueprint updated successfully");
                    return apiFunction.getBlueprintsByAuthor(author);
                })
                .then(data => {
                    blueprints = data.map(bp => ({ name: bp.name, points: bp.points }));
                    updateBlueprintTable();
                })
                .catch(() => alert("Error updating blueprint."));
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

        canvas.addEventListener("pointerdown", addPoint);
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

        $("#blueprint-name-display").text(`Blueprint Name: ${blueprintName}`);
        $("#blueprint-info").text("You can add points to the new blueprint using the canvas. Click Save/Update when done.");
    }

    function deleteBlueprint() {
        if (!blueprintName || !author) {
            alert("Author or Blueprint not selected!");
            return;
        }

        apiFunction.deleteBlueprint(author, blueprintName)
            .then(() => {
                alert("Blueprint successfully deleted.");
                return apiFunction.getBlueprintsByAuthor(author);
            })
            .then(newBlueprints => {
                blueprints = newBlueprints.map(bp => ({ name: bp.name, points: bp.points }));
                updateBlueprintTable();
            })
            .catch(() => alert("Error deleting blueprint."));
    }

    function clearCanvas() {
        if (!ctx) initCanvas();
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

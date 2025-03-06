apiclient=(function(){

    var APIRUL = "http://localhost:8080";

    return {
        getBlueprintsByAuthor: function (authname) {
            return new Promise(function (resolve, reject) {
                $.ajax({
                    url: APIRUL + "/blueprints/" + authname,
                    method: "GET",
                    dataType: "json",
                    success: function (data) {
                        resolve(data);
                    },
                    error: function (error) {
                        reject(error);
                    },
                });
            });
        },

        updateBlueprint: function (authname, bpname, blueprintData) {
            return new Promise(function (resolve, reject) {
                $.ajax({
                    url: APIRUL + "/blueprints/" + authname + "/" + bpname,
                    method: "PUT",
                    data: JSON.stringify(blueprintData),
                    contentType: "application/json",
                    success: function () {
                        resolve();
                    },
                    error: function (error) {
                        reject(error);
                    },
                });
            });
        },

        createBlueprint: function (newBlueprint) {
            return new Promise(function (resolve, reject) {
                $.ajax({
                    url: APIRUL + "/blueprints/",
                    method: "POST",
                    data: JSON.stringify(newBlueprint),
                    contentType: "application/json",
                    success: function () {
                        resolve();
                    },
                    error: function (error) {
                        reject(error);
                    },
                });
            });
        },

        deleteBlueprint: function (authname, bpname) {
            return new Promise(function (resolve, reject) {
                $.ajax({
                    url: APIRUL + "/blueprints/" + authname + "/" + bpname,
                    method: "DELETE",
                    success: function () {
                        resolve();
                    },
                    error: function (error) {
                        reject(error);
                    },
                });
            });
        }
    }
})();
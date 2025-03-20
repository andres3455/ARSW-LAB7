var apiclient = (function(){

    var apiBaseUrl = "http://localhost:8080";

    return {
        getBlueprintsByAuthor: function(authname) {
            return new Promise((resolve, reject) => {
                $.ajax({
                    url: `${apiBaseUrl}/blueprints/${authname}`,
                    method: "GET",
                    dataType: "json",
                    success: resolve,
                    error: reject
                });
            });
        },

        getBlueprintsByNameAndAuthor: function(authname, bpname) {
            return new Promise((resolve, reject) => {
                $.ajax({
                    url: `${apiBaseUrl}/blueprints/${authname}/${bpname}`,
                    method: "GET",
                    dataType: "json",
                    success: resolve,
                    error: reject
                });
            });
        },

        updateBlueprint: function(authname, bpname, blueprintData) {
            return new Promise((resolve, reject) => {
                $.ajax({
                    url: `${apiBaseUrl}/blueprints/${authname}/${bpname}`,
                    type: "PUT",
                    data: JSON.stringify(blueprintData),
                    contentType: "application/json",
                    success: resolve,
                    error: reject
                });
            });
        },

        createBlueprint: function(newBlueprint) {
            return new Promise((resolve, reject) => {
                $.ajax({
                    url: `${apiBaseUrl}/planos`,
                    type: "POST",
                    data: JSON.stringify(newBlueprint),
                    contentType: "application/json",
                    success: resolve,
                    error: reject
                });
            });
        },

        deleteBlueprint: function(authname, bpname) {
            return new Promise((resolve, reject) => {
                $.ajax({
                    url: `${apiBaseUrl}/blueprints/${authname}/${bpname}`,
                    type: "DELETE",
                    success: resolve,
                    error: reject
                });
            });
        },


        refreshBlueprints: function (authname) {
            return new Promise(function (resolve, reject) {
                $.ajax({
                    url: apiBaseUrl + "/blueprints/" + authname,
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
        }
    };
})();

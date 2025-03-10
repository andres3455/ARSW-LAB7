apiclient = (function(){

    var apiBaseUrl = "http://localhost:8080";

    return {
        getBlueprintsByAuthor: function (authname, callback) {
            $.ajax({
                url: apiBaseUrl + "/blueprints/" + authname,
                method: "GET",
                dataType: "json",
                success: function (data) {
                    callback(data);
                },
                error: function (xhr, status, error) {
                    console.error("Error fetching blueprints: ", error);
                    callback([]);
                }
            });
        },

        getBlueprintsByNameAndAuthor: function(authname, bpname, callback) {
            $.ajax({
                url: apiBaseUrl + "/blueprints/" + authname + "/" + bpname,
                method: "GET",
                dataType: "json",
                success: function (data) {
                    callback(data);
                },
                error: function (xhr, status, error) {
                    console.error("Error fetching blueprint: ", error);
                    callback(null);
                }
            });
        }
    }
})();

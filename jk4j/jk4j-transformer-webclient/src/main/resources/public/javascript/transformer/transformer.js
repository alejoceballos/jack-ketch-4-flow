(function() {
    var app = angular.module('jk4j-transformer', ['ui.codemirror']);


    app.factory('remoteApi', [
        '$http',
        function($http) {
            var url = "api/transform/";

            var RemoteApi = function() {
                this.transform = function(from) {
                    var options = {
                        method: 'POST',
                        url: url,
                        data: from
                    };

                    return $http(options).then(
                        function(response) {
                            return response.data;
                        }
                    ).catch(
                        function(err) {
                            console.log(err);
                        }
                    );
                };
            };

            return new RemoteApi();
        }
    ]);

    app.controller('TransformerController', [
        '$scope', 'remoteApi',
        function($scope, remoteApi) {
            console.log(">> TransformerController");

            this.data = data;

            this.applyTransformation = function() {
                remoteApi.transform(data.from).then(
                    function(to) {
                        data.to = JSON.stringify(to);
                    }
                );
            }
        }
    ]);

    var data = {
        from: '<Enter Violet XML>',
        to: '<Soon, your JSON transformation>'
    };

})();

(function() {
    var app = angular.module('jk4j', []);

    app.controller('TransformerController', function() {
        this.transformation = transformation;
    });

    var transformation = {
        from: 'Nothing',
        to: 'None'
    };
})();

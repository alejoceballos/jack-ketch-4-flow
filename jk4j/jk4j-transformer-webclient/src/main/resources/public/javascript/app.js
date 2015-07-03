(function() {
    var app = angular.module('jk4j-app',
        [
            'jk4j-transformer',
            'jk4j-menu-bar',
            'ngRoute',
            'ui.bootstrap',
            'ui.bootstrap.tpls',
            'textAngular',
            'LocalStorageModule',
            'angular-growl',
            'ngAnimate'
        ]);

    app.config(['$routeProvider', function($routeProvider) {
        'use strict';

        $routeProvider.
            when('/about', {
                templateUrl: '/javascript/about/about.tpl.html'
            }).
            when('/transform', {
                templateUrl: '/javascript/transformer/transformer.tpl.html',
                controller: 'TransformerController'
            }).
            otherwise({
                redirectTo: '/about'
            });
    }])
})();

define(['angular-route-resolver'], function(moment) {
    'use strict';

    var ERROR = {
        OK: 0
    };

    var angular = require('angular');
    var app = angular.module('app', ['ngRoute', 'controllers', 'routeResolverServices'])

    .factory('appServices', ['$rootScope', function($rootScope) {
        var appServices = {
            clone: function(obj) {
                return $.extend(true, {}, obj);
            },
            isNumeric: function(n) {
                return !isNaN(parseFloat(n)) && isFinite(n);
            },
            setActiveNavItem: function(item) {
                $rootScope.activeNavItem = item;
            }
        };
        return appServices;
    }])

    .directive("ngModel", ['$timeout', function($timeout) {
        return {
            restrict: 'A',
            priority: 0,
            link: function(scope, element, attr) {
                scope.$watch(attr.ngModel, function(value) {
                    $timeout(function() {
                        element.trigger("change");
                    }, 0, false);
                });
            }
          };
    }])

    .config(['$routeProvider', '$httpProvider', '$controllerProvider', '$compileProvider', '$filterProvider', '$provide', 'routeResolverProvider', function($routeProvider, $httpProvider, $controllerProvider, $compileProvider, $filterProvider, $provide, routeResolverProvider) {
        app.lazy = {
            controller: $controllerProvider.register,
            directive: $compileProvider.directive,
            filter: $filterProvider.register,
            factory: $provide.factory,
            service: $provide.service
        };

        var route = routeResolverProvider.route;

        $routeProvider
            .when('/home', route.resolve('home', 'HomeController'))
            .when('/channels', route.resolve('channels', 'ChannelsController'))
            .when('/channels/edit', route.resolve('channels-edit', 'ChannelsEditController'))
            .when('/channels/edit/:id', route.resolve('channels-edit', 'ChannelsEditController'))
            .when('/tokens', route.resolve('tokens', 'TokensController'))
            .when('/tokens/edit', route.resolve('tokens-edit', 'TokensEditController'))
            .when('/tokens/edit/:id', route.resolve('tokens-edit', 'TokensEditController'))
            .when('/api', route.resolve('api', 'ApiController'))
            .otherwise({
                redirectTo: '/home'
            });
    }]);

    app.init = function() {
        angular.bootstrap(document, ['app'], {
            strictDi: true
        });
    };

    return app;
});

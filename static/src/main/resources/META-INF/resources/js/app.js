define(['angular-route-resolver'], function(moment) {
    'use strict';

    var ERROR = {
        OK: 0,
        INVALID_INPUT_PARAMETERS: 1,
        OBJECT_LOOKUP_ERROR: 2,
        PERMISSION_DENIED: 3,
        AUTH_REQUIRED: 4
    };

    var angular = require('angular');
    var app = angular.module('app', ['ngRoute', 'controllers', 'routeResolverServices'])

    .factory('appServices', ['$rootScope', function($rootScope) {
        var appServices = {
            clone: function(obj) {
                return $.extend(true, {}, obj);
            },
            setSession: function(session) {
                $rootScope.session = session;
                if (session) {
                    $rootScope.isLoggedIn = true;
                    window.sessionStorage.setItem('session', JSON.stringify(session));
                } else {
                    $rootScope.isLoggedIn = false;
                    window.sessionStorage.removeItem('session');
                }
            },
            loadSession: function() {
                $rootScope.session = null;
                $rootScope.isLoggedIn = false;
                var session = window.sessionStorage.getItem('session');
                if (session) {
                    $rootScope.session = JSON.parse(session);
                    $rootScope.isLoggedIn = true;
                }
            },
            getToken: function() {
                if ($rootScope.session) {
                    return $rootScope.session.token;
                }
                return '';
            },
            getSessionUser: function() {
                if ($rootScope.session) {
                    return $rootScope.session.user;
                }
                return {};
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
            .when('/login', route.resolve('login', 'LoginController'))
            .when('/channels', route.resolve('channels', 'ChannelsController'))
            .when('/channels/edit', route.resolve('channels-edit', 'ChannelsEditController'))
            .when('/channels/edit/:id', route.resolve('channels-edit', 'ChannelsEditController'))
            .when('/tokens', route.resolve('tokens', 'TokensController'))
            .when('/tokens/edit', route.resolve('tokens-edit', 'TokensEditController'))
            .when('/tokens/edit/:id', route.resolve('tokens-edit', 'TokensEditController'))
            .when('/templates', route.resolve('templates', 'TemplatesController'))
            .when('/templates/edit', route.resolve('templates-edit', 'TemplatesEditController'))
            .when('/templates/edit/:id', route.resolve('templates-edit', 'TemplatesEditController'))
            .when('/templates/channels/edit/:templateId', route.resolve('channeltemplates-edit', 'ChannelTemplatesEditController'))
            .when('/templates/channels/edit/:templateId/:id', route.resolve('channeltemplates-edit', 'ChannelTemplatesEditController'))
            .when('/users', route.resolve('users', 'UsersController'))
            .when('/users/edit', route.resolve('users-edit', 'UsersEditController'))
            .when('/users/edit/:id', route.resolve('users-edit', 'UsersEditController'))
            .when('/config', route.resolve('config', 'ConfigController'))
            .when('/system', {redirectTo: '/users'})
            .when('/logs', route.resolve('logs', 'LogsController'))
            .when('/api/doc', route.resolve('api-doc', 'ApiDocController'))
            .when('/api/test', route.resolve('api-test', 'ApiTestController'))
            .when('/api', {redirectTo: '/api/doc'})
            .otherwise({redirectTo: '/home'});
            
        $httpProvider.interceptors.push(['$q', '$injector', '$location', 'appServices', function($q, $injector, $location, appServices) {
            return {
                'response': function(response) {
                    if (!response || !response.data || response.data.error === ERROR.AUTH_REQUIRED) {
                        appServices.setSession(null);
                        $location.path('/login');
                        return $q.reject(response);
                    }
                    return response;
                }
            };
        }]);
    }]);

    app.init = function() {
        angular.bootstrap(document, ['app'], {
            strictDi: true
        });
    };

    return app;
});

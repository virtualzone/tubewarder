define(['angular-route-resolver'], function(moment) {
    'use strict';

    var ERROR = {
        OK: 0,
        INVALID_INPUT_PARAMETERS: 1,
        OBJECT_LOOKUP_ERROR: 2,
        PERMISSION_DENIED: 3,
        AUTH_REQUIRED: 4,
        FIELD_REQUIRED: 101,
        FIELD_NAME_ALREADY_EXISTS: 102,
        FIELD_INVALID: 103
    };

    var angular = require('angular');
    var app = angular.module('app', ['ngRoute', 'controllers', 'routeResolverServices'])

    .factory('appServices', ['$rootScope', '$injector', function($rootScope, $injector) {
        var appServices = {
            checkPasswordPolicy: function(password, form, field) {
                password = password.trim();
                if (password.length < 8) {
                    form[field].$setValidity('invalid', false);
                    appServices.focus('#'+field);
                    appServices.error('Password must have at least 8 characters');
                    return false;
                } else if (!password.match(/[0-9]/g)) {
                    form[field].$setValidity('invalid', false);
                    appServices.focus('#'+field);
                    appServices.error('Password must contain at least 1 number (0-9)');
                    return false;
                } else if (!password.match(/[A-Z]/g)) {
                    form[field].$setValidity('invalid', false);
                    appServices.focus('#'+field);
                    appServices.error('Password must contain at least 1 uppercase character (A-Z)');
                    return false;
                } else if (!password.match(/[a-z]/g)) {
                    form[field].$setValidity('invalid', false);
                    appServices.focus('#'+field);
                    appServices.error('Password must contain at least 1 lowercase character (a-z)');
                    return false;
                }
                return true;
            },
            focus: function(selector) {
                window.setTimeout(function() {
                    $(selector).first().focus();
                }, 100);
            },
            showMsgBox: function(msg, type) {
                var alert = $('#primary-alert');
                if (alert.length > 0) {
                    alert.remove();
                }
                alert = $(document.createElement('div'));
                var button = $(document.createElement('button'));
                var span = $(document.createElement('span'));
                var text = $(document.createTextNode(msg));
                span.attr({
                    'aria-hidden': 'true'
                }).html('&times;');
                button.attr({
                    'type': 'button',
                    'class': 'close',
                    'data-dismiss': 'alert',
                    'aria-label': 'Close'
                }).append(span);
                alert.attr({
                    'class': 'alert alert-'+type+' alert-dismissible',
                    'role': 'alert',
                    'id': 'primary-alert'
                }).append(button, text);
                var h1 = $('h1');
                if (h1.length) {
                    h1.first().after(alert);
                }
            },
            alert: function(msg) {
                this.showMsgBox(msg, 'warning');
            },
            success: function(msg) {
                this.showMsgBox(msg, 'success');
            },
            error: function(msg) {
                this.showMsgBox(msg, 'danger');
            },
            httpError: function() {
                appServices.error('Error communicating with the server. Please check your network connection and try again.');
            },
            handleGenericErrors: function(data, cbOk, cbInvalidParams, cbUnhandledError) {
                if (typeof data == 'undefined' || data === null) {
                    appServices.error('Invalid response from server. Please check your entries and try again.');
                } else if (data.error == ERROR.OK) {
                    if (cbOk) cbOk(data);
                } else if (data.error == ERROR.OBJECT_LOOKUP_ERROR) {
                    appServices.error('The object you tried to update or assign does not exist.');
                } else if (data.error == ERROR.PERMISSION_DENIED) {
                    appServices.error('You don\'t have permission to perform the requested action.');
                } else if (data.error == ERROR.AUTH_REQUIRED) {
                    appServices.error('You\'re not authenticated. Please log in again.');
                } else if (data.error == ERROR.INVALID_INPUT_PARAMETERS) {
                    if (cbInvalidParams) cbInvalidParams(data.fieldErrors);
                } else {
                    if (cbUnhandledError) cbUnhandledError(data.error);
                }
            },
            post: function(rs, payload, cbOk, cbInvalidParams, cbUnhandledError) {
                var $http = $injector.get('$http');
                $http.post(rs, payload).success(function(data) {
                    appServices.handleGenericErrors(data, cbOk, cbInvalidParams, cbUnhandledError);
                }).error(appServices.httpError);
            },
            clone: function(obj) {
                return $.extend(true, {}, obj);
            },
            setLoading: function(b) {
                $rootScope.loading = b;
            },
            getErrors: function() {
                return ERROR;
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
            .when('/groups', route.resolve('groups', 'GroupsController'))
            .when('/groups/edit', route.resolve('groups-edit', 'GroupsEditController'))
            .when('/groups/edit/:id', route.resolve('groups-edit', 'GroupsEditController'))
            .when('/config', route.resolve('config', 'ConfigController'))
            .when('/system', {redirectTo: '/users'})
            .when('/logs', route.resolve('logs', 'LogsController'))
            .when('/queue', route.resolve('queue', 'QueueController'))
            .when('/api/test', route.resolve('api-test', 'ApiTestController'))
            .when('/api', {redirectTo: '/api/test'})
            .when('/me', route.resolve('me', 'MeController'))
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

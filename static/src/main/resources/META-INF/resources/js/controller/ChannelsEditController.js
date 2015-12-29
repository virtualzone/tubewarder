define(['angular', 'app'], function(angular, app) {
	'use strict';

    app.lazy.controller('ChannelsEditController', ['$scope', '$http', '$location', '$routeParams', 'appServices', function($scope, $http, $location, $routeParams, appServices) {
        appServices.setActiveNavItem('channels');
        
        $scope.model = {
            id: '',
            name: '',
            outputHandler: 'SYSOUT',
            sysout: {
                id: '',
                type: 'SYSOUT',
                prefix: '',
                suffix: ''
            },
            email: {
                id: '',
                type: 'EMAIL',
                smtpServer: '',
                port: 25,
                auth: false,
                username: '',
                password: '',
                security: 'NONE',
                contentType: 'text/plain'
            }
        };
        
        var storeConfig = function(cb) {
            var payload = {
                object: { }
            };
            var url = '';
            if ($scope.model.outputHandler == 'SYSOUT') {
                url = '/rs/sysoutoutputhandlerconfiguration/set';
                payload.object = $scope.model.sysout;
            } else if ($scope.model.outputHandler == 'EMAIL') {
                url = '/rs/emailoutputhandlerconfiguration/set';
                payload.object = $scope.model.email;
            }
            $http.post(url, payload).success(function(data) {
                cb(data.id);
            });
        };
        
        $scope.submit = function(form) {
            storeConfig(function(configId) {
                var payload = {
                    object: {
                        id: $scope.model.id,
                        name: $scope.model.name,
                        outputHandler: $scope.model.outputHandler,
                        config: {
                            id: configId,
                            type: $scope.model.outputHandler
                        }
                    }
                };
                $http.post('/rs/channel/set', payload).success(function(data) {
                    $location.path('/channels');
                });
            });
		};
        
        if ($routeParams.id) {
            var payload = {
                id: $routeParams.id
            };
            $http.get('/rs/channel/get', {params: payload}).success(function(data) {
                var channel = data.channels[0];
                $scope.model.id = channel.id;
                $scope.model.name = channel.name;
                $scope.model.outputHandler = channel.outputHandler;
                if (channel.outputHandler == 'SYSOUT') {
                    $scope.model.sysout.id = channel.config.id;
                    $scope.model.sysout.prefix = channel.config.prefix;
                    $scope.model.sysout.suffix = channel.config.suffix;
                } else if (channel.outputHandler == 'EMAIL') {
                    $scope.model.email.id = channel.config.id;
                    $scope.model.email.smtpServer = channel.config.smtpServer;
                    $scope.model.email.port = channel.config.port;
                    $scope.model.email.auth = channel.config.auth;
                    $scope.model.email.username = channel.config.username;
                    $scope.model.email.password = channel.config.password;
                    $scope.model.email.security = channel.config.security;
                    $scope.model.email.contentType = channel.config.contentType;
                } 
            });
        }
    }]);
});

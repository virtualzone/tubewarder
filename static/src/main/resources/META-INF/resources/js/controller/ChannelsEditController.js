define(['angular', 'app'], function(angular, app) {
	'use strict';

    app.lazy.controller('ChannelsEditController', ['$scope', '$http', '$location', '$routeParams', 'appServices', function($scope, $http, $location, $routeParams, appServices) {
        appServices.setActiveNavItem('channels');
        
        $scope.model = {
            id: '',
            name: '',
            outputHandler: 'SYSOUT',
            sysout: {
                id: 'SYSOUT',
                prefix: '',
                suffix: ''
            },
            email: {
                id: 'EMAIL',
                smtpServer: '',
                port: 25,
                auth: false,
                username: '',
                password: '',
                security: 'NONE',
                contentType: 'text/plain'
            }
        };
        
        var getConfig = function(id) {
            if (id == "SYSOUT") {
                return $scope.model.sysout;
            } else if (id == "EMAIL") {
                return $scope.model.email;
            } else {
                return {};
            }
        };
        
        $scope.submit = function(form) {
            var payload = {
                token: appServices.getToken(),
                object: {
                    id: $scope.model.id,
                    name: $scope.model.name,
                    config: getConfig($scope.model.outputHandler)
                }
            };
            $http.post('/rs/channel/set', payload).success(function(data) {
                $location.path('/channels');
            });
		};
        
        if ($routeParams.id) {
            var payload = {
                token: appServices.getToken(),
                id: $routeParams.id
            };
            $http.get('/rs/channel/get', {params: payload}).success(function(data) {
                var channel = data.channels[0];
                $scope.model.id = channel.id;
                $scope.model.name = channel.name;
                $scope.model.outputHandler = channel.config.id;
                if (channel.config.id == 'SYSOUT') {
                    $scope.model.sysout.prefix = channel.config.prefix;
                    $scope.model.sysout.suffix = channel.config.suffix;
                } else if (channel.config.id == 'EMAIL') {
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

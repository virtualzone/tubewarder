define(['angular', 'app'], function(angular, app) {
	'use strict';

    app.lazy.controller('ChannelsEditController', ['$scope', '$http', '$location', '$routeParams', 'appServices', function($scope, $http, $location, $routeParams, appServices) {
        appServices.setActiveNavItem('channels');
        
        $scope.model = {
            id: '',
            name: '',
            outputHandler: ''
        };
        $scope.handlers = [];
        $scope.configOptions = [];
        
        var getConfig = function() {
            var config = {};
            config.id = $scope.model.outputHandler;
            for (var i=0; i<$scope.configOptions.length; i++) {
                var option = $scope.configOptions[i];
                config[option.id] = option.value;
            }
            return config;
        };
        
        var loadOutputHandlers = function(cb) {
            var payload = {
                token: appServices.getToken()
            };
            $http.get('/rs/outputhandler/get', {params: payload}).success(function(data) {
                $scope.handlers = data.outputHandlers;
                cb();
            });
        };
        
        $scope.renderAvailableConfigOptions = function() {
            for (var i=0; i<$scope.handlers.length; i++) {
                var handler = $scope.handlers[i];
                if (handler.id == $scope.model.outputHandler) {
                    $scope.configOptions = angular.copy(handler.configOptions);
                    for (var j=0; j<$scope.configOptions.length; j++) {
                        var option = $scope.configOptions[j];
                        if (option.defaultValue) {
                            option.value = option.defaultValue;
                        } else {
                            option.value = '';
                        }
                    }
                }
            }
        };
        
        var setConfigOptions = function(config) {
            $scope.model.outputHandler = config.id;
            $scope.renderAvailableConfigOptions();
            for (var j=0; j<$scope.configOptions.length; j++) {
                var option = $scope.configOptions[j];
                option.value = config[option.id];
            }
        };
        
        $scope.submit = function(form) {
            var payload = {
                token: appServices.getToken(),
                object: {
                    id: $scope.model.id,
                    name: $scope.model.name,
                    config: getConfig()
                }
            };
            $http.post('/rs/channel/set', payload).success(function(data) {
                $location.path('/channels');
            });
		};
        
        var load = function() {
            if ($routeParams.id) {
                var payload = {
                    token: appServices.getToken(),
                    id: $routeParams.id
                };
                $http.get('/rs/channel/get', {params: payload}).success(function(data) {
                    var channel = data.channels[0];
                    $scope.model.id = channel.id;
                    $scope.model.name = channel.name;
                    setConfigOptions(channel.config);
                });
            }
        };
        
        loadOutputHandlers(load);
    }]);
});

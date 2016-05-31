define(['angular', 'app'], function(angular, app) {
	'use strict';

    app.lazy.controller('ChannelsEditController', ['$scope', '$http', '$location', '$routeParams', 'appServices', function($scope, $http, $location, $routeParams, appServices) {
        appServices.setActiveNavItem('channels');
        
        $scope.model = {
            id: '',
            name: '',
            groupId: '',
            rewriteRecipientName: '${recipientName}',
            rewriteRecipientAddress: '${recipientAddress}',
            rewriteSubject: '${subject}',
            rewriteContent: '${content}',
            outputHandler: ''
        };
        $scope.groups = [];
        $scope.handlers = [];
        $scope.configOptions = [];
        
        var getConfig = function() {
            var config = {};
            config.id = $scope.model.outputHandler;
            for (var i=0; i<$scope.configOptions.length; i++) {
                var option = $scope.configOptions[i];
                if (option.type == 'bool' && !option.value) {
                    config[option.id] = false;
                } else {
                    config[option.id] = option.value;
                }
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
        
        $scope.resetRecipientName = function() {
            $scope.model.rewriteRecipientName = '${recipientName}';
        };
        
        $scope.resetRecipientAddress = function() {
            $scope.model.rewriteRecipientAddress = '${recipientAddress}';
        };
        
        $scope.resetSubject = function() {
            $scope.model.rewriteSubject = '${subject}';
        };
        
        $scope.resetContent = function() {
            $scope.model.rewriteContent = '${content}';
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
            appServices.setLoading(true);
            var payload = {
                token: appServices.getToken(),
                object: {
                    id: $scope.model.id,
                    name: $scope.model.name,
                    group: {
                        id: $scope.model.groupId
                    },
                    rewriteRecipientName: $scope.model.rewriteRecipientName,
                    rewriteRecipientAddress: $scope.model.rewriteRecipientAddress,
                    rewriteSubject: $scope.model.rewriteSubject,
                    rewriteContent: $scope.model.rewriteContent,
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
                    $scope.model.groupId = channel.group.id;
                    setConfigOptions(channel.config);
                    appServices.setLoading(false);
                });
            } else {
                appServices.setLoading(false);
            }
        };
        
        var loadGroups = function() {
            var payload = {
                token: appServices.getToken()
            };
            $http.get('/rs/group/get/small', {params: payload}).success(function(data) {
                $scope.groups = data.groups;
                load();
            });
        };
        
        loadOutputHandlers(loadGroups);
        $('[data-toggle="popover"]').popover();
    }]);
});

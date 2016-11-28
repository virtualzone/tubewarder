define(['angular', 'app'], function(angular, app) {
	'use strict';

    app.lazy.controller('ChannelsEditController', ['$scope', '$http', '$location', '$routeParams', 'appServices', function($scope, $http, $location, $routeParams, appServices) {
        appServices.setActiveNavItem('channels');
        
        $scope.model = {
            id: '',
            name: '',
            groupId: '',
            rewriteRecipientName: '{{recipientName}}',
            rewriteRecipientAddress: '{{recipientAddress}}',
            rewriteSubject: '{{subject}}',
            rewriteContent: '{{content}}',
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
            $scope.model.rewriteRecipientName = '{{recipientName}}';
        };
        
        $scope.resetRecipientAddress = function() {
            $scope.model.rewriteRecipientAddress = '{{recipientAddress}}';
        };
        
        $scope.resetSubject = function() {
            $scope.model.rewriteSubject = '{{subject}}';
        };
        
        $scope.resetContent = function() {
            $scope.model.rewriteContent = '{{content}}';
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
            appServices.post('/rs/channel/set', payload,
                function(data) {
                    $location.path('/channels');
                },
                function(fieldErrors) {
                    if (fieldErrors.name) {
                        form.name.$setValidity('invalid', false);
                        appServices.focus('#name');
                        if ($.inArray(appServices.getErrors().FIELD_NAME_ALREADY_EXISTS, fieldErrors.name) !== -1) {
                            appServices.error('Name already exists');
                        } else if ($.inArray(appServices.getErrors().FIELD_REQUIRED, fieldErrors.name) !== -1) {
                            appServices.error('Name is required');
                        }
                    }
                    var config = getConfig();
                    for (var i=0; i<$scope.configOptions.length; i++) {
                        var configOption = $scope.configOptions[i];
                        if (fieldErrors['config.'+configOption.id]) {
                            form['option-'+configOption.id].$setValidity('invalid', false);
                            appServices.focus('#option-'+configOption.id);
                            if ($.inArray(appServices.getErrors().FIELD_NAME_ALREADY_EXISTS, fieldErrors['config.'+configOption.id]) !== -1) {
                                appServices.error(configOption.label+' already exists');
                            } else if ($.inArray(appServices.getErrors().FIELD_REQUIRED, fieldErrors['config.'+configOption.id]) !== -1) {
                                appServices.error(configOption.label+' is required');
                            } else if ($.inArray(appServices.getErrors().FIELD_INVALID, fieldErrors['config.'+configOption.id]) !== -1) {
                                appServices.error(configOption.label+' is invalid');
                            }
                        }
                    }
                    appServices.setLoading(false);
                }
            );
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
                    $scope.model.rewriteRecipientName = channel.rewriteRecipientName;
                    $scope.model.rewriteRecipientAddress = channel.rewriteRecipientAddress;
                    $scope.model.rewriteSubject = channel.rewriteSubject;
                    $scope.model.rewriteContent = channel.rewriteContent;
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

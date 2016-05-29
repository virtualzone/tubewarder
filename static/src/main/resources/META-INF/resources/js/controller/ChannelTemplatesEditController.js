define(['angular', 'app'], function(angular, app) {
	'use strict';

    app.lazy.controller('ChannelTemplatesEditController', ['$scope', '$http', '$location', '$routeParams', 'appServices', function($scope, $http, $location, $routeParams, appServices) {
        appServices.setActiveNavItem('templates');
        
        $scope.model = {
            id: '',
            templateId: $routeParams.templateId,
            templateName: '',
            channelId: '',
            subject: '',
            content: '',
            senderAddress: '',
            senderName: ''
        };
        $scope.channels = [];
        $scope.channelTemplates = [];

        $scope.submit = function(form) {
            var payload = {
                token: appServices.getToken(),
                object: {
                    id: $scope.model.id,
                    template: {
                        id: $scope.model.templateId
                    },
                    channel: {
                        id: $scope.model.channelId
                    },
                    subject: $scope.model.subject,
                    content: $scope.model.content,
                    senderAddress: $scope.model.senderAddress,
                    senderName: $scope.model.senderName
                }
            };
            $http.post('/rs/channeltemplate/set', payload).success(function(data) {
                $location.path('/templates/edit/' + $routeParams.templateId);
            });
		};
        
        // Requires $scope.channels and $scope.channelTemplates to be both loaded
        var limitChannelSelect = function() {
            for (var i=0; i<$scope.channels.length; i++) {
                var channel = $scope.channels[i];
                channel.disabled = false;
                for (var j=0; j<$scope.channelTemplates.length; j++) {
                    var ct = $scope.channelTemplates[j];
                    if ((ct.channel.id == channel.id) && (ct.template.id == $routeParams.templateId) && (!$routeParams.id || (ct.id != $routeParams.id))) {
                        channel.disabled = true;
                    }
                }
            }
        };
        
        var loadChannels = function() {
            var payload = {
                token: appServices.getToken()
            };
            $http.get('/rs/channel/get', {params: payload}).success(function(data) {
                $scope.channels = data.channels;
                limitChannelSelect();
                loadTemplate();
            });
        };
        
        var loadTemplate = function() {
            var payload = {
                token: appServices.getToken(),
                id: $routeParams.templateId
            };
            $http.get('/rs/template/get', {params: payload}).success(function(data) {
                var template = data.templates[0];
                $scope.model.templateName = template.name;
                load();
            });
        };
        
        var restoreCurrentChannelTemplateData = function() {
            for (var i=0; i<$scope.channelTemplates.length; i++) {
                var ct = $scope.channelTemplates[i];
                if (ct.id == $routeParams.id) {
                    $scope.model.id = ct.id;
                    $scope.model.templateId = ct.template.id;
                    $scope.model.channelId = ct.channel.id;
                    $scope.model.subject = ct.subject;
                    $scope.model.content = ct.content;
                    $scope.model.senderAddress = ct.senderAddress;
                    $scope.model.senderName = ct.senderName;
                }
            }
        };
        
        var loadAllChannelTemplates = function() {
            var payload = {
                token: appServices.getToken(),
                templateId: $routeParams.templateId
            };
            $http.get('/rs/channeltemplate/get', {params: payload}).success(function(data) {
                $scope.channelTemplates = data.channelTemplates;
                if ($routeParams.id) {
                    restoreCurrentChannelTemplateData();
                }
                loadChannels();
            });
        };
        
        var load = function() {
            if ($routeParams.id) {
                var payload = {
                    token: appServices.getToken(),
                    id: $routeParams.id
                };
                $http.get('/rs/channeltemplate/get', {params: payload}).success(function(data) {
                    var ct = data.channelTemplates[0];
                    $scope.model.id = ct.id;
                    $scope.model.templateId = ct.template.id;
                    $scope.model.channelId = ct.channel.id;
                    $scope.model.subject = ct.subject;
                    $scope.model.content = ct.content;
                    $scope.model.senderAddress = ct.senderAddress;
                    $scope.model.senderName = ct.senderName;
                });
            }
        };
        
        loadAllChannelTemplates();
    }]);
});

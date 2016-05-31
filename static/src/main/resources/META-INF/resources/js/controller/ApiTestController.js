define(['angular', 'app'], function(angular, app) {
	'use strict';

    app.lazy.controller('ApiTestController', ['$scope', '$http', 'appServices', function($scope, $http, appServices) {
        appServices.setActiveNavItem('api');
        
        $scope.model = {
            token: '',
            templateId: '',
            channelId: '',
            recipient: {
                name: '',
                address: ''
            },
            model: [],
            attachments: [],
            keyword: '',
            details: '',
            echo: true
        };
        $scope.tokens = [];
        $scope.templates = [];
        $scope.channels = [];
        $scope.payload = {};
        $scope.response = null;
        $scope.submitButtonText = '';
        
        var getTemplateName = function(id) {
            for (var i=0; i<$scope.templates.length; i++) {
                if ($scope.templates[i].id == id) {
                    return $scope.templates[i].name;
                }
            }
            return '';
        };
        
        var getChannelName = function(id) {
            for (var i=0; i<$scope.channels.length; i++) {
                if ($scope.channels[i].id == id) {
                    return $scope.channels[i].name;
                }
            }
            return '';
        };
        
         $scope.generateSendPayload = function() {
            $scope.payload = {
                token: ($scope.model.token ? $scope.model.token : ''),
                template: getTemplateName($scope.model.templateId),
                channel: getChannelName($scope.model.channelId),
                recipient: {
                    name: $scope.model.recipient.name,
                    address: $scope.model.recipient.address
                },
                model: $scope.model.model,
                attachments: $scope.model.attachments,
                keyword: $scope.model.keyword,
                details: $scope.model.details,
                echo: $scope.model.echo
            };
            $scope.response = null;
            $scope.submitButtonText = 'Send';
        };
        
        $scope.addModelParameter = function() {
            $scope.model.model.push({key: '', value: ''});
            $scope.generateSendPayload();
        };
        
        $scope.addAttachment = function() {
            $scope.model.attachments.push({filename: '', contentType: '', payload: ''});
            $scope.generateSendPayload();
        };
        
        $scope.fileChanged = function(element) {
            $scope.$apply(function(scope) {
                if (element && element.files && element.files.length == 1 && element.files[0]) {
                    var i = $('input[type="file"]').index(element);
                    var file = element.files[0];
                    scope.generateSendPayload();
                    scope.model.attachments[i] = {
                        filename: file.name,
                        contentType: file.type,
                        payload: ''
                    };
                    var reader = new FileReader();
                    reader.onload = function(e) {
                        scope.model.attachments[i].payload = reader.result.substr(reader.result.indexOf(',')+1);
                    };
                    reader.readAsDataURL(file);
                }
            });
        };
        
        $scope.onTemplateSelect = function() {
            $scope.channelId = '';
            $scope.channels = [];
            $scope.generateSendPayload();
            var payload = {
                token: appServices.getToken(),
                templateId: $scope.model.templateId
            };
            $http.get('/rs/channeltemplate/get', {params: payload}).success(function(data) {
                $scope.channels = [];
                for (var i=0; i<data.channelTemplates.length; i++) {
                    $scope.channels.push({id: data.channelTemplates[i].channel.id, name: data.channelTemplates[i].channel.name});
                }
            });
        };
        
        var loadTokens = function() {
            var payload = {
                token: appServices.getToken()
            };
            $http.get('/rs/apptoken/get', {params: payload}).success(function(data) {
                $scope.tokens = data.tokens;
            });
        };
        
        var loadTemplates = function() {
            var payload = {
                token: appServices.getToken()
            };
            $http.get('/rs/template/get', {params: payload}).success(function(data) {
                $scope.templates = data.templates;
                appServices.setLoading(false);
            });
        };
        
        $scope.submit = function(form) {
            appServices.setLoading(true);
            $scope.submitButtonText = 'Sending...';
            $scope.generateSendPayload();
            $http.post('/rs/send', $scope.payload).success(function(data) {
                $scope.response = data;
                $scope.submitButtonText = 'Success! See response below.';
                appServices.setLoading(false);
            });
        };
        
        $scope.generateSendPayload();
        loadTokens();
        loadTemplates();
    }]);
});

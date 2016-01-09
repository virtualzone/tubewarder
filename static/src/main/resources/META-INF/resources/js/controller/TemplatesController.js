define(['angular', 'app'], function(angular, app) {
	'use strict';

    app.lazy.controller('TemplatesController', ['$scope', '$http', 'appServices', function($scope, $http, appServices) {
        appServices.setActiveNavItem('templates');
        
        $scope.model = {
            templates: []
        };
        
        var makeReadableChannelList = function(template) {
            template.channels = "";
            for (var i=0; i<template.channelTemplates.length; i++) {
                var ct = template.channelTemplates[i];
                if (template.channels !== "") {
                    template.channels += ", ";
                }
                template.channels += ct.channel.name;
            }
        };
        
        var load = function() {
            var payload = {
                token: appServices.getToken()
            };
            $http.get('/rs/template/get', {params: payload}).success(function(data) {
                $scope.model.templates = data.templates;
                for (var i=0; i<$scope.model.templates.length; i++) {
                    makeReadableChannelList($scope.model.templates[i]);
                }
            });
        };
        
        $scope.deleteTemplate = function(id) {
            if (!confirm("Delete this template?")) return;
            var payload = {
                token: appServices.getToken(),
                id: id
            };
            $http.post('/rs/template/delete', payload).success(function(data) {
                load();
            });
        };
        
        load();
    }]);
});

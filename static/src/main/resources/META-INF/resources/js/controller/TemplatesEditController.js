define(['angular', 'app'], function(angular, app) {
	'use strict';

    app.lazy.controller('TemplatesEditController', ['$scope', '$http', '$location', '$routeParams', 'appServices', function($scope, $http, $location, $routeParams, appServices) {
        appServices.setActiveNavItem('templates');
        
        $scope.model = {
            id: '',
            name: '',
            channelTemplates: []
        };

        $scope.submit = function(form) {
            var payload = {
                object: {
                    id: $scope.model.id,
                    name: $scope.model.name
                }
            };
            $http.post('/rs/template/set', payload).success(function(data) {
                $location.path('/templates');
            });
		};
        
        var load = function() {
            if ($routeParams.id) {
                var payload = {
                    id: $routeParams.id
                };
                $http.get('/rs/template/get', {params: payload}).success(function(data) {
                    var template = data.templates[0];
                    $scope.model.id = template.id;
                    $scope.model.name = template.name;
                    $scope.model.channelTemplates = template.channelTemplates; 
                });
            }
        };
        
        $scope.deleteChannelTemplate = function(id) {
            if (!confirm("Delete this channel association?")) return;
            var payload = {
                id: id
            };
            $http.post('/rs/channeltemplate/delete', payload).success(function(data) {
                load();
            });
        };
        
        load();
    }]);
});

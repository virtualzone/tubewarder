define(['angular', 'app'], function(angular, app) {
	'use strict';

    app.lazy.controller('GroupsEditController', ['$scope', '$http', '$location', '$routeParams', 'appServices', function($scope, $http, $location, $routeParams, appServices) {
        appServices.setActiveNavItem('system');
        
        $scope.model = {
            id: '',
            name: '',
            members: []
        };
        
        $scope.submit = function(form) {
            var payload = {
                token: appServices.getToken(),
                object: {
                    id: $scope.model.id,
                    name: $scope.model.name
                }
            };
            $http.post('/rs/group/set', payload).success(function(data) {
                $location.path('/groups');
            });
		};
        
        if ($routeParams.id) {
            var payload = {
                token: appServices.getToken(),
                id: $routeParams.id
            };
            $http.get('/rs/group/get', {params: payload}).success(function(data) {
                var group = data.groups[0];
                $scope.model.id = group.id;
                $scope.model.name = group.name;
                $scope.model.members = group.members;
            });
        }
    }]);
});

define(['angular', 'app'], function(angular, app) {
	'use strict';

    app.lazy.controller('GroupsController', ['$scope', '$http', 'appServices', function($scope, $http, appServices) {
        appServices.setActiveNavItem('system');
        
        $scope.model = {
            groups: []
        };
        
        var load = function() {
            var payload = {
                token: appServices.getToken()
            };
            $http.get('/rs/group/get', {params: payload}).success(function(data) {
                $scope.model.groups = data.groups;
            });
        };
        
        $scope.deleteGroup = function(id) {
            if (!confirm("Delete this group?")) return;
            var payload = {
                token: appServices.getToken(),
                id: id
            };
            $http.post('/rs/group/delete', payload).success(function(data) {
                load();
            });
        };
        
        load();
    }]);
});

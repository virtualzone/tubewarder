define(['angular', 'app'], function(angular, app) {
	'use strict';

    app.lazy.controller('UsersController', ['$scope', '$http', 'appServices', function($scope, $http, appServices) {
        appServices.setActiveNavItem('users');
        
        $scope.model = {
            users: []
        };
        
        var load = function() {
            $http.get('/rs/user/get', {}).success(function(data) {
                $scope.model.users = data.users;
            });
        };
        
        $scope.deleteUser = function(id) {
            if (!confirm("Delete this user?")) return;
            var payload = {
                id: id
            };
            $http.post('/rs/user/delete', payload).success(function(data) {
                load();
            });
        };
        
        load();
    }]);
});

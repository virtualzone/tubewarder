define(['angular', 'app'], function(angular, app) {
	'use strict';

    app.lazy.controller('UsersController', ['$scope', '$http', 'appServices', function($scope, $http, appServices) {
        appServices.setActiveNavItem('system');
        
        $scope.model = {
            users: []
        };
        
        var load = function() {
            var payload = {
                token: appServices.getToken()
            };
            $http.get('/rs/user/get', {params: payload}).success(function(data) {
                $scope.model.users = data.users;
                appServices.setLoading(false);
            });
        };

        $scope.canDelete = function(id) {
            if (id == appServices.getSessionUser().id) {
                return false;
            }
            return true;
        };
        
        $scope.deleteUser = function(id) {
            if (!confirm("Delete this user?")) return;
            appServices.setLoading(true);
            var payload = {
                token: appServices.getToken(),
                id: id
            };
            $http.post('/rs/user/delete', payload).success(function(data) {
                load();
            });
        };
        
        load();
    }]);
});

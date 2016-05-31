define(['angular', 'app'], function(angular, app) {
	'use strict';

    app.lazy.controller('TokensController', ['$scope', '$http', 'appServices', function($scope, $http, appServices) {
        appServices.setActiveNavItem('tokens');
        
        $scope.model = {
            tokens: []
        };
        
        var load = function() {
            appServices.setLoading(true);
            var payload = {
                token: appServices.getToken()
            };
            $http.get('/rs/apptoken/get', {params: payload}).success(function(data) {
                $scope.model.tokens = data.tokens;
                appServices.setLoading(false);
            });
        };
        
        $scope.deleteToken = function(id) {
            if (!confirm("Delete this token?")) return;
            appServices.setLoading(true);
            var payload = {
                token: appServices.getToken(),
                id: id
            };
            $http.post('/rs/apptoken/delete', payload).success(function(data) {
                load();
            });
        };
        
        load();
    }]);
});

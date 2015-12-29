define(['angular', 'app'], function(angular, app) {
	'use strict';

    app.lazy.controller('TokensController', ['$scope', '$http', 'appServices', function($scope, $http, appServices) {
        appServices.setActiveNavItem('tokens');
        
        $scope.model = {
            tokens: []
        };
        
        var load = function() {
            $http.get('/rs/apptoken/get', {}).success(function(data) {
                $scope.model.tokens = data.tokens;
            });
        };
        
        $scope.deleteToken = function(id) {
            if (!confirm("Delete this token?")) return;
            var payload = {
                id: id
            };
            $http.post('/rs/apptoken/delete', payload).success(function(data) {
                load();
            });
        };
        
        load();
    }]);
});

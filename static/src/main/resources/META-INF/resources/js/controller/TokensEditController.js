define(['angular', 'app'], function(angular, app) {
	'use strict';

    app.lazy.controller('TokensEditController', ['$scope', '$http', '$location', '$routeParams', 'appServices', function($scope, $http, $location, $routeParams, appServices) {
        appServices.setActiveNavItem('tokens');
        
        $scope.model = {
            id: '',
            name: ''
        };

        $scope.submit = function(form) {
            var payload = {
                token: appServices.getToken(),
                object: {
                    id: $scope.model.id,
                    name: $scope.model.name
                }
            };
            $http.post('/rs/apptoken/set', payload).success(function(data) {
                $location.path('/tokens');
            });
		};
        
        if ($routeParams.id) {
            var payload = {
                token: appServices.getToken(),
                id: $routeParams.id
            };
            $http.get('/rs/apptoken/get', {params: payload}).success(function(data) {
                var token = data.tokens[0];
                $scope.model.id = token.id;
                $scope.model.name = token.name; 
            });
        }
    }]);
});

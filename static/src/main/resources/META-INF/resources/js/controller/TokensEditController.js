define(['angular', 'app'], function(angular, app) {
	'use strict';

    app.lazy.controller('TokensEditController', ['$scope', '$http', '$location', '$routeParams', 'appServices', function($scope, $http, $location, $routeParams, appServices) {
        appServices.setActiveNavItem('tokens');
        
        $scope.model = {
            id: '',
            name: ''
        };

        $scope.submit = function(form) {
            appServices.setLoading(true);
            var payload = {
                token: appServices.getToken(),
                object: {
                    id: $scope.model.id,
                    name: $scope.model.name
                }
            };
            appServices.post('/rs/apptoken/set', payload,
                function(data) {
                    $location.path('/tokens');
                },
                function(fieldErrors) {
                    if (fieldErrors.name) {
                        form.name.$setValidity('invalid', false);
                        appServices.focus('#name');
                        if ($.inArray(appServices.getErrors().FIELD_REQUIRED, fieldErrors.name) !== -1) {
                            appServices.error('Name is required');
                        }
                    }
                    appServices.setLoading(false);
                }
            );
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
                appServices.setLoading(false); 
            });
        } else {
            appServices.setLoading(false);
        }
    }]);
});

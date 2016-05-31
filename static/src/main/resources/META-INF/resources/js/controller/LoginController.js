define(['angular', 'app'], function(angular, app) {
	'use strict';

    app.lazy.controller('LoginController', ['$scope', '$http', '$location', '$routeParams', 'appServices', function($scope, $http, $location, $routeParams, appServices) {
        appServices.setActiveNavItem('');
        appServices.setLoading(false);
        
        $scope.model = {
            username: '',
            password: ''
        };
        
        $scope.onPasswordChange = function(form) {
			form.password.$setValidity('invalid', true);
			form.password.$validate();
		};
        
        $scope.submit = function(form) {
            appServices.setLoading(true);
            var payload = {
                username: $scope.model.username,
                password: $scope.model.password
            };
            $http.post('/rs/auth', payload).success(function(data) {
                if (data && !data.error) {
                    var session = {
						token: data.token,
						user: data.user
					};
					appServices.setSession(session);
                    $location.path('/home');
                } else {
                    form.password.$setValidity('invalid', false);
                    appServices.setLoading(false);
                }
            });
		};
    }]);
});

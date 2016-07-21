define(['angular', 'app'], function(angular, app) {
	'use strict';

    app.lazy.controller('LoginController', ['$scope', '$location', '$routeParams', 'appServices', function($scope, $location, $routeParams, appServices) {
        appServices.setActiveNavItem('');

        appServices.get('/rs/checklicense', function(data) {
            appServices.setLoading(false);
            if (!data.termsAccepted) {
                appServices.setSession(null);
                $location.path('/terms');
            }
        });
        
        $scope.model = {
            username: '',
            password: ''
        };
        
        $scope.submit = function(form) {
            appServices.setLoading(true);
            var payload = {
                username: $scope.model.username,
                password: $scope.model.password
            };
            appServices.post('/rs/auth', payload,
                function(data) {
                    var session = {
						token: data.token,
						user: data.user
					};
					appServices.setSession(session);
                    $location.path('/home');
                },
                function(fieldErrors) {
                    appServices.error('Invalid username and/or password.');
                    form.password.$setValidity('invalid', false);
                    appServices.setLoading(false);
                }
            );
		};
    }]);
});

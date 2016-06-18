
define(['angular', 'app'], function(angular, app) {
	'use strict';

    app.lazy.controller('MeController', ['$scope', '$http', '$location', '$routeParams', 'appServices', function($scope, $http, $location, $routeParams, appServices) {
        appServices.setActiveNavItem('me');
        
        $scope.model = {
            username: appServices.getSessionUser().username,
            displayName: appServices.getSessionUser().displayName,
            password: '',
            changePassword: false
        };
        
        $scope.submit = function(form) {
            if (!$scope.model.changePassword) {
                return;
            }
            if ($scope.model.changePassword) {
                if (!appServices.checkPasswordPolicy($scope.model.password, form, 'password')) {
                    return;
                }
            }
            appServices.setLoading(true);
            var payload = {
                token: appServices.getToken(),
                password: ($scope.model.changePassword ? $scope.model.password : '')
            };
            appServices.post('/rs/user/setpassword', payload,
                function(data) {
                    appServices.success('Profile updated successfully!');
                    appServices.setLoading(false);
                },
                function(fieldErrors) {
                    appServices.setLoading(false);
                }
            );
		};

        appServices.setLoading(false);
    }]);
});

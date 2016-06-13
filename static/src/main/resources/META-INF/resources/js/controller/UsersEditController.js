define(['angular', 'app'], function(angular, app) {
	'use strict';

    app.lazy.controller('UsersEditController', ['$scope', '$http', '$location', '$routeParams', 'appServices', function($scope, $http, $location, $routeParams, appServices) {
        appServices.setActiveNavItem('system');
        
        $scope.model = {
            id: '',
            username: '',
            displayName: '',
            password: '',
            changePassword: false,
            enabled: true,
            allowAppTokens: false,
            allowChannels: false,
            allowTemplates: false,
            allowSystemConfig: false,
            allowLogs: false
        };
        
        $scope.isRequirePassword = function() {
		    return ($scope.model.changePassword) || ($scope.model.id === '');
		};

        $scope.onUsernameChange = function(form) {
			form.username.$setValidity('invalid', true);
			form.username.$validate();
		};
        
        $scope.submit = function(form) {
            appServices.setLoading(true);
            var payload = {
                token: appServices.getToken(),
                object: {
                    id: $scope.model.id,
                    username: $scope.model.username,
                    displayName: $scope.model.displayName,
                    password: (($scope.model.id === '' || $scope.model.changePassword) ? $scope.model.password : ''),
                    enabled: $scope.model.enabled,
                    allowAppTokens: $scope.model.allowAppTokens,
                    allowChannels: $scope.model.allowChannels,
                    allowTemplates: $scope.model.allowTemplates,
                    allowSystemConfig: $scope.model.allowSystemConfig,
                    allowLogs: $scope.model.allowLogs
                }
            };
            appServices.post('/rs/user/set', payload,
                function(data) {
                    $location.path('/users');
                },
                function(fieldErrors) {
                    if (fieldErrors.username) {
                        form.username.$setValidity('invalid', false);
                        appServices.focus('#username');
                        if ($.inArray(appServices.getErrors().FIELD_NAME_ALREADY_EXISTS)) {
                            appServices.error('Username already exists');
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
            $http.get('/rs/user/get', {params: payload}).success(function(data) {
                var user = data.users[0];
                $scope.model.id = user.id;
                $scope.model.username = user.username;
                $scope.model.displayName = user.displayName;
                $scope.model.enabled = user.enabled;
                $scope.model.allowAppTokens = user.allowAppTokens;
                $scope.model.allowChannels = user.allowChannels;
                $scope.model.allowTemplates = user.allowTemplates;
                $scope.model.allowSystemConfig = user.allowSystemConfig;
                $scope.model.allowLogs = user.allowLogs;
                appServices.setLoading(false);
            });
        } else {
            appServices.setLoading(false);
        }
    }]);
});

define(['angular', 'app'], function(angular, app) {
	'use strict';

	var controllers = angular.module('controllers', []);

	controllers.run(['$location', '$http', '$rootScope', 'appServices', function($location, $http, $rootScope, appServices) {
	    $rootScope.loading = true;
		appServices.loadSession();

        if ($rootScope.isLoggedIn) {
            var payload = {
                token: appServices.getToken()
            };
            $http.post('/rs/ping', payload).success(function(data) {
                if (data.error) {
                    appServices.setSession(null);
                    $location.path('/login');
                }
            });
        }
	}]);

	controllers.controller('RootController', ['$scope', '$rootScope', '$location', '$http', 'appServices', function($scope, $rootScope, $location, $http, appServices) {
		$rootScope.$on('$routeChangeStart', function(event, next, current) {
			$rootScope.loading = true;
            if (!$rootScope.isLoggedIn) {
				if (next.originalPath != '/login') {
					$location.path('/login');
				}
			}
		});
        
        $scope.logout = function() {
		    var payload = {
		        token: appServices.getToken()
		    };
		    $http.post('/rs/logout', payload).success(function(response) {
                appServices.setSession(null);
                $location.path('/login');
            });
		};
	}]);

    return controllers;
});

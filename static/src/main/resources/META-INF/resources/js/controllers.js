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
        $scope.isRouteChanging = false;
		
		$rootScope.$on('$routeChangeStart', function(event, next, current) {
			$scope.isRouteChanging = true;
            if (!$rootScope.isLoggedIn) {
				if (next.originalPath != '/login') {
					$location.path('/login');
				}
			}
		});

		$rootScope.$on('$routeChangeSuccess', function(next, current) {
			$scope.isRouteChanging = false;
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

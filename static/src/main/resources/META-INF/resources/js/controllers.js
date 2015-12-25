define(['angular', 'app'], function(angular, app) {
	'use strict';

	var controllers = angular.module('controllers', []);

	controllers.run(['$location', '$http', '$rootScope', 'appServices', function($location, $http, $rootScope, appServices) {
	    
	}]);

	controllers.controller('RootController', ['$scope', '$rootScope', '$location', '$http', 'appServices', function($scope, $rootScope, $location, $http, appServices) {
        $scope.isRouteChanging = false;
		
		$rootScope.$on('$routeChangeStart', function(event, next, current) {
			$scope.isRouteChanging = true;
		});

		$rootScope.$on('$routeChangeSuccess', function(next, current) {
			$scope.isRouteChanging = false;
		});
	}]);

    return controllers;
});

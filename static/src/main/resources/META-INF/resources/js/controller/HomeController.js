define(['angular', 'app'], function(angular, app) {
	'use strict';

    app.lazy.controller('HomeController', ['$scope', '$http', 'appServices', function($scope, $http, appServices) {
        appServices.setActiveNavItem('home');
    }]);
});

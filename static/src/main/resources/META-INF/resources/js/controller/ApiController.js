define(['angular', 'app'], function(angular, app) {
	'use strict';

    app.lazy.controller('ApiController', ['$scope', '$http', 'appServices', function($scope, $http, appServices) {
        appServices.setActiveNavItem('api');
        
    }]);
});

define(['angular', 'app'], function(angular, app) {
	'use strict';

    app.lazy.controller('ApiDocController', ['$scope', '$http', 'appServices', function($scope, $http, appServices) {
        appServices.setActiveNavItem('api');
        
    }]);
});

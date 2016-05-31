define(['angular', 'app'], function(angular, app) {
	'use strict';

    app.lazy.controller('QueueController', ['$scope', '$http', '$interval', '$rootScope', 'appServices', function($scope, $http, $interval, $rootScope, appServices) {
        appServices.setActiveNavItem('system');
        
        $scope.status = {};
        
        var load = function() {
            appServices.setLoading(true);
            var payload = {
                token: appServices.getToken()
            };
            $http.get('/rs/queue/status', {params: payload}).success(function(data) {
                $scope.status = data.status;
                appServices.setLoading(false);
            });
        };
        
        load();
        var intervalPromise = $interval(load, 2000);
        
        $rootScope.$on("$routeChangeStart", function() {
            $interval.cancel(intervalPromise); 
        });
    }]);
});

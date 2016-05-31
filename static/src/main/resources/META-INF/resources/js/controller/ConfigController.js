define(['angular', 'app'], function(angular, app) {
	'use strict';

    app.lazy.controller('ConfigController', ['$scope', '$http', 'appServices', function($scope, $http, appServices) {
        appServices.setActiveNavItem('system');
        
        $scope.model = {
            items: []
        };
        $scope.saveSuccess = false;
        
        $scope.submit = function(form) {
            appServices.setLoading(true);
            $scope.saveSuccess = false;
            var payload = {
                token: appServices.getToken(),
                items: $scope.model.items
            };
            $http.post('/rs/config/set', payload).success(function(data) {
                $scope.saveSuccess = true;
                appServices.setLoading(false);
            });
        };
        
        var load = function() {
            var payload = {
                token: appServices.getToken()
            };
            $http.get('/rs/config/get', {params: payload}).success(function(data) {
                for (var i=0; i<data.items.length; i++) {
                    var item = data.items[i];
                    if (item.type == "INT") {
                        item.value = parseInt(item.value);
                    }
                }
                $scope.model.items = data.items;
                appServices.setLoading(false);
            });
        };
        
        load();
    }]);
});

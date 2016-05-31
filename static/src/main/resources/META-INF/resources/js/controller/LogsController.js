define(['angular', 'moment', 'app'], function(angular, moment, app) {
	'use strict';

    app.lazy.controller('LogsController', ['$scope', '$http', 'appServices', function($scope, $http, appServices) {
        appServices.setActiveNavItem('logs');
        
        var endDate = moment();
        var startDate = moment().subtract(1, 'month');
        $scope.model = {
            logs: [],
            startDate: startDate.format('YYYY-MM-DD HH:mm:ss'),
            endDate: endDate.format('YYYY-MM-DD HH:mm:ss'),
            keyword: '',
            searchString: '',
            log: {}
        };
        
        $scope.showLog = function(id) {
            appServices.setLoading(true);
            $scope.model.log = {};
            var payload = {
                token: appServices.getToken(),
                id: id
            };
            $http.get('/rs/log/get', {params: payload}).success(function(data) {
                $scope.model.log = data.logs[0];
                $('#logModal').modal('show');
                appServices.setLoading(false);
            });
        };
        
        var load = function() {
            appServices.setLoading(true);
            var payload = {
                token: appServices.getToken(),
                startDate: $scope.model.startDate,
                endDate: $scope.model.endDate,
                keyword: $scope.model.keyword,
                searchString: $scope.model.searchString
            };
            $http.get('/rs/log/get', {params: payload}).success(function(data) {
                $scope.model.logs = data.logs;
                appServices.setLoading(false);
            });
        };
        
        load();
        $('.datetimepicker').datetimepicker({
            format: 'YYYY-MM-DD HH:mm:ss'
        });
        
        $scope.applyFilter = function() {
            load();
        };
    }]);
});

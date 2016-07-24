define(['angular', 'app'], function(angular, app) {
	'use strict';

    app.lazy.controller('ConfigController', ['$scope', '$http', 'appServices', function($scope, $http, appServices) {
        appServices.setActiveNavItem('system');
        
        $scope.model = {
            items: []
        };
        $scope.saveSuccess = false;

        var isTermsAccepted = function() {
            for (var i=0; i<$scope.model.items.length; i++) {
                var item = $scope.model.items[i];
                if (item.key == "TERMS_ACCEPTED") {
                    return (item.value && item.value == '1' ? true : false);
                }
            }
        };

        var checkNextKey = function(keys, cbSuccess) {
            if (keys.length === 0) {
                cbSuccess();
                return;
            }
            var payload = {
                token: appServices.getToken(),
                id: keys[0]
            };
            $http.post('/rs/checklicensekey', payload).success(function(data) {
                if (data.error) {
                    appServices.error('Invalid license key(s).');
                    appServices.setLoading(false);
                } else {
                    checkNextKey(keys.slice(1), cbSuccess);
                }
            });
        };

        var checkLicenseKeys = function(cbSuccess) {
            var keys = [];
            for (var i=0; i<$scope.model.items.length; i++) {
                var item = $scope.model.items[i];
                if (item.key.indexOf("LICENSE_KEY_") === 0) {
                    var licenseKey = item.value;
                    if (licenseKey) {
                        keys.push(licenseKey);
                    }
                }
            }
            checkNextKey(keys, cbSuccess);
        };
        
        $scope.submit = function(form) {
            appServices.setLoading(true);
            checkLicenseKeys(function() {
                var payload = {
                    token: appServices.getToken(),
                    items: $scope.model.items
                };
                $http.post('/rs/config/set', payload).success(function(data) {
                    appServices.success('Settings successfully saved.');
                    appServices.setLoading(false);
                    if (!isTermsAccepted()) {
                        appServices.logout();
                    }
                });
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
                    } else if (item.type == "BOOL") {
                        item.value = (item.value && item.value == '1' ? true : false);
                    }
                }
                $scope.model.items = data.items;
                appServices.setLoading(false);
            });
        };
        
        load();
    }]);
});

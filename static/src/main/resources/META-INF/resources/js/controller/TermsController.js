define(['angular', 'app'], function(angular, app) {
	'use strict';

    app.lazy.controller('TermsController', ['$scope', '$location', '$routeParams', 'appServices', function($scope, $location, $routeParams, appServices) {
        appServices.setActiveNavItem('');
        appServices.setLoading(false);
        
        $scope.model = {
            accept: false
        };
        
        $scope.submit = function(form) {
            appServices.setLoading(true);
            var payload = {
                termsAccepted: $scope.model.accept
            };
            appServices.post('/rs/acceptterms', payload,
                function(data) {
                    $location.path('/login');
                }
            );
		};
    }]);
});

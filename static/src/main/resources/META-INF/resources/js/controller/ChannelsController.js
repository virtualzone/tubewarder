define(['angular', 'app'], function(angular, app) {
	'use strict';

    app.lazy.controller('ChannelsController', ['$scope', '$http', 'appServices', function($scope, $http, appServices) {
        appServices.setActiveNavItem('channels');
        
        $scope.model = {
            channels: []
        };
        
        $http.get('/rs/channel/get', {}).success(function(data) {
            $scope.model.channels = data.channels;
            for (var i=0; i<$scope.model.channels.length; i++) {
                var channel = $scope.model.channels[i];
                if (channel.outputHandler == 'SYSOUT') channel.outputHandlerReadable = 'Console';
                if (channel.outputHandler == 'EMAIL') channel.outputHandlerReadable = 'Email';
            }
        });
    }]);
});

define(['angular', 'app'], function(angular, app) {
	'use strict';

    app.lazy.controller('ChannelsController', ['$scope', '$http', 'appServices', function($scope, $http, appServices) {
        appServices.setActiveNavItem('channels');
        
        $scope.model = {
            channels: []
        };
        
        var load = function() {
            var payload = {
                token: appServices.getToken()
            };
            $http.get('/rs/channel/get', {params: payload}).success(function(data) {
                $scope.model.channels = data.channels;
                for (var i=0; i<$scope.model.channels.length; i++) {
                    var channel = $scope.model.channels[i];
                    if (channel.config.id == 'SYSOUT') channel.outputHandlerReadable = 'Console';
                    if (channel.config.id == 'EMAIL') channel.outputHandlerReadable = 'Email';
                }
            });
        };
        
        $scope.deleteChannel = function(configId, channelId) {
            if (!confirm("Delete this channel?")) return;
            var payload = {
                id: channelId,
                token: appServices.getToken()
            };
            $http.post('/rs/channel/delete', payload).success(function(data) {
                load();
            });
        };
        
        load();
    }]);
});

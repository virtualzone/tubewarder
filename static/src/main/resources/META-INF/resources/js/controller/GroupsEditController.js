define(['angular', 'app', 'typeahead'], function(angular, app, typeahead) {
	'use strict';

    app.lazy.controller('GroupsEditController', ['$scope', '$http', '$location', '$routeParams', 'appServices', function($scope, $http, $location, $routeParams, appServices) {
        appServices.setActiveNavItem('system');
        
        $scope.model = {
            id: '',
            name: '',
            members: []
        };
        
        $scope.submit = function(form) {
            appServices.setLoading(true);
            var payload = {
                token: appServices.getToken(),
                object: {
                    id: $scope.model.id,
                    name: $scope.model.name
                }
            };
            $http.post('/rs/group/set', payload).success(function(data) {
                $location.path('/groups');
            });
		};
        
        var load = function() {
            var payload = {
                token: appServices.getToken(),
                id: $routeParams.id
            };
            $http.get('/rs/group/get', {params: payload}).success(function(data) {
                var group = data.groups[0];
                $scope.model.id = group.id;
                $scope.model.name = group.name;
                $scope.model.members = group.members;
                appServices.setLoading(false);
            });
        };
        
        $scope.addUserToGroup = function(userId, groupId) {
            appServices.setLoading(true);
            var payload = {
                token: appServices.getToken(),
                userId: userId,
                groupId: groupId
            };
            $http.post('/rs/group/adduser', payload).success(function(data) {
                load();
            });
        };
        
        $scope.removeUserFromGroup = function(userId, groupId) {
            if (!confirm("Remove this user from the group?")) return;
            appServices.setLoading(true);
            var payload = {
                token: appServices.getToken(),
                userId: userId,
                groupId: groupId
            };
            $http.post('/rs/group/removeuser', payload).success(function(data) {
                load();
            });
        };
        
        $('#username').typeahead({
            hint: true,
            highlight: true,
            minLength: 1
        },
        {
            name: 'users',
            display: 'value',
            source: new Bloodhound({
                datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
                queryTokenizer: Bloodhound.tokenizers.whitespace,
                remote: {
                    url: '/rs/user/search/%QUERY?token='+appServices.getToken(),
                    wildcard: '%QUERY',
                    transform: function(resp) {
                        return $.map(resp.users, function(v, k) {
                            return {id: k, value: v};
                        });
                    }
                }
            })
        }).bind('typeahead:select', function(ev, suggestion) {
            $scope.$apply(function() {
                $('#username').typeahead('val', '');
                $scope.addUserToGroup(suggestion.id, $scope.model.id);
            });
        });
        
        if ($routeParams.id) {
            load();
        } else {
            appServices.setLoading(false);
        }
    }]);
});

define(function(require) {
    'use strict';

    var routeResolver = function() {
        this.$get = function () {
            return this;
        };

        this.route = function() {
            var resolve = function(html, ctl) {
                var routeDef = {
                    templateUrl: '/partial/'+html+'.html?v='+new Date().getTime(),
                    controller: ctl,
                    resolve: {
                        load: ['$q', '$rootScope', function ($q, $rootScope) {
                            var deferred = $q.defer();
                            require(['/js/controller/'+ctl+'.js'], function() {
                                $rootScope.$apply(function() {
                                    deferred.resolve();
                                });
                            });
                            return deferred.promise;
                        }]
                    }
                };
                return routeDef;
            };
            return {
                resolve: resolve
            };
        }();
    };

    angular.module('routeResolverServices', []).provider('routeResolver', routeResolver);
});

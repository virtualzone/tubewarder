require.config({
	paths: {
        'jquery': 'lib/jquery-2.1.4.min',
        'angular': 'lib/angular-1.4.8.min',
		'angular-route': 'lib/angular-route-1.4.8.min',
		'angular-route-resolver': 'lib/angular-route-resolver',
        'bootstrap': 'lib/bootstrap-3.3.6.min'
	},
	shim: {
        'bootstrap': {
			deps: ['jquery'],
		},
		'angular': {
            deps: ['jquery'],
			exports: 'angular'
		},
		'angular-route': {
			deps: ['angular']
		},
		'angular-route-resolver': {
			deps: ['angular-route']
		},
		'app': {
			deps: [
				'bootstrap',
				'angular-route',
				'angular-route-resolver'
			],
			exports: 'app'
		},
		'controllers': {
			deps: ['app'],
			exports: 'controllers'
		}
	},
	urlArgs: "v="+new Date().getTime()
});

require(['controllers', 'app'], function(controllers, app) {
	app.init();
});

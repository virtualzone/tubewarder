require.config({
	paths: {
        'jquery': 'lib/jquery-2.1.4.min',
        'angular': 'lib/angular-1.4.8.min',
		'angular-route': 'lib/angular-route-1.4.8.min',
		'angular-route-resolver': 'lib/angular-route-resolver',
        'bootstrap': 'lib/bootstrap-3.3.6.min',
        'moment': 'lib/moment-2.11.2.min',
        'bootstrap-datetimepicker': 'lib/bootstrap-datetimepicker-4.17.37.min',
        'autofill-event': 'lib/autofill-event'
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
        'bootstrap-datetimepicker': {
            deps: ['bootstrap', 'moment']
        },
        'autofill-event': {
            deps: ['angular', 'jquery']
        },
		'app': {
			deps: [
				'bootstrap',
                'bootstrap-datetimepicker',
				'angular-route',
				'angular-route-resolver',
                'autofill-event'
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

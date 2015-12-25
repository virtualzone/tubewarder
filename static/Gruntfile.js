'use strict';

module.exports = function(grunt) {
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        bootlint: {
            options: {
                stoponerror: false,
                relaxerror: [
                    'W005' // jQuery not found
                ]
            },
            files: [
                'src/main/resources/META-INF/resources/*.html'
            ]
        },
        jshint: {
            all: [
                'src/main/resources/META-INF/resources/js/*.js',
                'src/main/resources/META-INF/resources/js/controller/*.js'
            ]
        },
        compass: {
            dist: {
                options: {
                    sassDir: 'src/main/resources/META-INF/resources/scss',
                    cssDir: 'src/main/resources/META-INF/resources/css',
                    outputStyle: 'compressed',
                    noLineComments: true
                }
            }
        },
        requirejs: {
            compile: {
                options: {
                    logLevel: 0,
                    waitSeconds: 120,
                    baseUrl: 'src/main/resources/META-INF/resources/js',
                    mainConfigFile: 'src/main/resources/META-INF/resources/js/config.js',
                    name: 'config',
                    out: 'src/main/resources/META-INF/resources/js/main.js'
                }
            }
        }
    });

    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-compass');
    grunt.loadNpmTasks('grunt-contrib-requirejs');
    grunt.loadNpmTasks('grunt-bootlint');

    //grunt.registerTask('build', ['jshint', 'bootlint', 'compass', 'requirejs']);
    grunt.registerTask('build', ['jshint', 'bootlint', 'compass']);
    grunt.registerTask('default', ['build']);
}

module.exports = function(grunt) { 'use strict';

    grunt.initConfig({
        /**
         * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
         */
        bower: {
            install: {
                options: {
                    targetDir: './bower_components'
                }
            }
        },

        /**
         * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
         */
        concat: {
            vendor: {
                src: [
                    './bower_components/jquery/jquery.js',
                    './bower_components/angular/angular.js',
                    './bower_components/angular-route/angular-route.js',
                    './bower_components/angular-bootstrap/ui-bootstrap.js',
                    './bower_components/angular-bootstrap/ui-bootstrap-tpls.js',
                    './bower_components/ngInfiniteScroll/build/ng-infinite-scroll.js',
                    './bower_components/angular-dragdrop/src/angular-dragdrop.js',
                    './bower_components/moment/moment.js',
                    './bower_components/angular-moment/angular-moment.js',
                    './bower_components/ng-flow/src/angular-flow.js',
                    './bower_components/rangy/rangy-core.js',
                    './bower_components/rangy/rangy-selectionsaverestore.js',
                    './bower_components/textAngular/textAngular.js',
                    './bower_components/textAngular/textAngular-sanitize.js',
                    './bower_components/textAngular/textAngularSetup.js',
                    './bower_components/angular-local-storage/angular-local-storage.js',
                    './bower_components/angular-growl-v2/angular-growl.js',
                    './bower_components/angular-animate/angular-animate.js',
                    './bower_components/codemirror/codemirror.js',
                    './bower_components/angular-ui-codemirror/ui-codemirror.js'
                ],
                dest: './src/main/resources/public/javascript/vendor.js'
            }
        },

        /**
         * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
         */
        copy: {
            bootstrapFonts: {
                cwd: './bower_components/bootstrap/dist/fonts',
                src: [ 'glyphicons-halflings-regular.ttf', 'glyphicons-halflings-regular.woff' ],
                dest: './src/main/resources/public/fonts',
                expand: true
            },
            bootstrapCss: {
                cwd: './bower_components/bootstrap/dist/css',
                src: [ 'bootstrap.css', 'bootstrap.css.map' ],
                dest: './src/main/resources/public/stylesheets',
                expand: true
            },
            textAngularCss: {
                cwd: './bower_components/textAngular',
                src: [ 'textAngular.css' ],
                dest: './src/main/resources/public/stylesheets',
                expand: true
            },
            awesomeFonts: {
                cwd: './bower_components/font-awesome',
                src: [ 'fontawesome-webfont.ttf', 'fontawesome-webfont.woff' ],
                dest: './src/main/resources/public/fonts',
                expand: true
            },
            awesomeCss: {
                cwd: './bower_components/font-awesome',
                src: [ 'font-awesome.css' ],
                dest: './src/main/resources/public/stylesheets',
                expand: true
            },
            angularGrowl: {
                cwd: './bower_components/angular-growl-v2',
                src: [ 'angular-growl.css' ],
                dest: './src/main/resources/public/stylesheets',
                expand: true
            },
            codeMirror: {
                cwd: './bower_components/codemirror/lib',
                src: [ 'codemirror.css' ],
                dest: './src/main/resources/public/stylesheets',
                expand: true
            }
        }
    });

    /**
     *
     */
    grunt.loadNpmTasks('grunt-bower-task');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-copy');

    /**
     *
     */
    grunt.registerTask('default', [ 'bower', 'concat', 'copy' ]);

    /**
     *
     */
    grunt.registerTask('build', [ 'bower', 'concat', 'copy' ]);

};
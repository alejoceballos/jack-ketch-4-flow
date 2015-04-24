// Set of JSHint directives
/* global describe: true */
/* global beforeEach: true */
/* global it: true */
/* global expect: true */

"use strict";

var Q = require('q');

describe('Q with all', function() {

    it('Should return a value after all have finished', function(done) {
        function f1() { return 'f1'; }
        function f2() { return 'f2'; }

        Q.all([f1(), f2()]).then(
            function(arg) {
                expect(arg instanceof Array).toBe(true);
                expect(arg.length).toBe(2);
                expect(arg[0]).toBe('f1');
                expect(arg[1]).toBe('f2');
            }
        ).catch(
            function(err) {
                expect(err).toBe('Should not have raised an error');
            }
        ).finally(done);
    });

    it('Should start many async processes but return only when all have returned', function(done) {
        function f1() { return 'f1'; }
        function f2() { return 'f2'; }

        Q.try(
            function() {
                var promise1 = Q.try(f1);
                var promise2 = Q.try(f2);

                return [ promise1, promise2 ];
            }
        ).then(
            function(promises) {
                var f1Result;
                var f2Result;

                promises[0].then(
                    function(f1Res) {
                        f1Result = f1Res;
                        return promises[1];
                    }
                ).then(
                    function(f2Res) {
                        f2Result = f2Res;
                        return [ f1Result, f2Result ];
                    }
                ).then(
                    function() {
                        expect(f1Result).toBe('f1');
                        expect(f2Result).toBe('f2');
                    }
                ).catch(
                    function(err) {
                        expect(err).toBe('Should not have raised an error 1');
                        done();
                    }
                ).finally(done);
            }
        ).catch(
            function(err) {
                expect(err).toBe('Should not have raised an error 2');
                done();
            }
        );
    });

    it('Put many promises in an array and execute them asynchronously', function(done) {
        function returnI(i) {
            var deferred = Q.defer();
            deferred.resolve(i);
            return deferred.promise;
        }

        function multiPromise() {
            var deferred = Q.defer();

            var promises = [];

            for (var i = 0; i < 10; i++) {
                promises.push(returnI(i));
            }

            Q.all(promises).then(
                function(arg) {
                    deferred.resolve(
                        function() {
                            return arg;
                        }
                    );
                }
            );

            return deferred.promise;
        }

        Q.try(multiPromise()).then(
            function(arg) {
                expect(arg).toBeDefined();
            }
        ).catch(
            function(err) {
                expect(err).toBe('Should not have risen an error')
            }
        ).finally(done);
    });

});

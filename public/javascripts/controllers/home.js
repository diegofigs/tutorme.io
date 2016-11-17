/**
 * Created by diegofigs on 10/7/16.
 */
'use strict';

/**
 * @ngdoc function
 * @name publicApp.controller:HomeCtrl
 * @description
 * # HomeCtrl
 * Controller of the Home Page of TutorMe.io
 */
angular.module('publicApp')
    .controller('HomeCtrl', [
        '$scope',
        'auth',
        'courses',
        function ($scope, auth, courses) {
            $scope.user = auth.currentUser();
            $scope.courses = courses.courses;
        }
    ]
);

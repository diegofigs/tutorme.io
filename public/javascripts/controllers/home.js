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
        '$location',
        'auth',
        'courses',
        function ($scope, $location, auth, courses) {
            $scope.user = auth.currentUser();
            $scope.courses = courses.courses;
            $scope.getSections = function(course){
                courses.getSections(course).success(function(){
                    $location.path('/sections');
                });
            }
        }
    ]
);

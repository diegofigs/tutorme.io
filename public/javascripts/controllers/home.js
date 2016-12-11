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
        '$route',
        'auth',
        'courses',
        function ($scope, $location, $route, auth, courses) {
            $scope.user = auth.currentUser();
            $scope.courses = courses.getCurrentCourses();
            $scope.new_courses = courses.getAvailableSections();
            $scope.getSections = function(course){
                courses.getSections(course.id).success(function(){
                    $location.path('/sections');
                });
            };
        }
    ]
);

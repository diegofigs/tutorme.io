/**
 * Created by diegofigs on 12/7/16.
 */
'use strict';

/**
 * @ngdoc function
 * @name publicApp.controller:CourseCtrl
 * @description
 * # CourseCtrl
 * Controller of the Courses Page
 */
angular.module('publicApp')
    .controller('ExploreCtrl', [
            '$scope',
            '$location',
            'auth',
            'courses',
            'lessons',
            function ($scope, $location, auth, courses) {
                $scope.user = auth.currentUser();
                $scope.courses = courses.getCurrentCourses();

            }
        ]
    );
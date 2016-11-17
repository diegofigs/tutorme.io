/**
 * Created by diegofigs on 10/10/16.
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
    .controller('CourseCtrl', [
            '$scope',
            'auth',
            'courses',
            function ($scope, auth, courses) {
                $scope.user = auth.currentUser();
                $scope.sections = courses.sections;
            }
        ]
    );

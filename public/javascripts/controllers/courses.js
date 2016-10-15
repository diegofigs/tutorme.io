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
            function ($scope, auth) {
                $scope.user = auth.currentUser();
            }
        ]
    );

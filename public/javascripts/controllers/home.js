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
            $scope.selectedSection = {
                enroll_id: 0,
                drop_id: 0
            };
            $scope.selectedCourse = {
                tutor_id: 0,
                title: '',
                description: ''
            };
            $scope.deletedCourse = {
                id: 0
            };
            $scope.getSections = function(course){
                courses.getSections(course.id).success(function(){
                    $location.path('/sections');
                });
            };
            $scope.enrollSection = function(){
                courses.enrollSection($scope.selectedSection.enroll_id, $scope.user.id).success(function(){
                    $scope.selectedSection.enroll_id = 0;
                    $route.reload();
                });
            };
            $scope.dropSection = function(){
                courses.dropSection($scope.selectedSection.drop_id, $scope.user.id).success(function(){
                    $scope.selectedSection.drop_id = 0;
                    $route.reload();
                });
            };
            $scope.createCourse = function(){
                $scope.selectedCourse.tutor_id = auth.currentUser().id;
                courses.createCourse($scope.selectedCourse).success(function(){
                    $scope.selectedCourse = {
                        tutor_id: 0,
                        title: '',
                        description: ''
                    };
                    $route.reload();
                });
            };
            $scope.deleteCourse = function(){
                courses.deleteCourse($scope.deletedCourse.id).success(function(){
                    $scope.deletedCourse = {
                        id: 0
                    };
                    $route.reload();
                });
            };
        }
    ]
);

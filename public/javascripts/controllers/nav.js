/**
 * Created by diegofigs on 10/7/16.
 */
'use strict';

/**
 * @ngdoc function
 * @name publicApp.controller:NavCtrl
 * @description
 * # NavCtrl
 * Controller of the Navbar
 */
angular.module('publicApp')
    .controller('NavCtrl', [
        '$scope',
        '$location',
        'auth',
        'courses',
        function ($scope, $location, auth, courses) {
            $scope.currentUser = auth.currentUser;
            $scope.isLoggedIn = auth.isLoggedIn;
            $scope.logout = function(){
                auth.logout();
                $location.path('/');
            };
            $scope.currentCourse = courses.getCurrentCourse;
            $scope.currentSection = courses.getCurrentSection;
            $scope.getCourses = function(user){
                if(user.type == 0){
                    courses.getTutorCourses(user.id);
                }
                else{
                    courses.getStudentCourses(user.id);
                }
                $location.path('/home');
            };
            $scope.getSections = function(course){
                courses.getSections(course.id).success(function(data){
                    $location.path('/sections');
                });
            };
            $scope.getAllCourses = function(user){
                courses.getCourses(user.id).success(function(){
                    $location.path('/explore');
                });
            };
        }
    ]
);
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
            '$location',
            '$route',
            'auth',
            'courses',
            function ($scope, $location, $route, auth, courses) {
                $scope.user = auth.currentUser();
                $scope.course = courses.getCurrentCourse();
                $scope.sections = courses.getCurrentSections();
                $scope.selectedSection = {
                    delete_id: 0
                };
                $scope.getSectionDetails = function(section){
                    courses.getSection(section.id).success(function(data){
                        $location.path('/lessons/'+data.id);
                    });
                };
                $scope.createSection = function(){
                    courses.createSection($scope.course.id, $scope.user.id).success(function(){
                        $route.reload();
                    });
                };
                $scope.deleteSection = function(){
                    courses.deleteSection($scope.selectedSection.delete_id).success(function(){
                        $route.reload();
                    });
                }
            }
        ]
    );

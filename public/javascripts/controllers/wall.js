/**
 * Created by Andres on 10/8/16.
 */
'use strict';

/*/**
 * @ngdoc function
 * @name publicApp.controller:WallCtrl
 * @description
 * # WallCtrl
 * Controller of the Wall of TutorMe.io
 */
angular.module('publicApp').controller('WallCtrl', [
    '$scope',
    '$route',
    'auth',
    'mailbox',
    'wall',
    function ($scope, $route, auth, mailbox, wall) {

        // var courses = dummyCoursesService.getDummyCourses();
        //
        // $scope.courses = dummyCoursesService.getDummyCourses();
        //
        // $scope.activeWall=courses[0];
        //
        // $scope.getActiveWall = function(){
        //     return $scope.activeWall;
        // };
        //
        // $scope.setActiveWall = function(courseId){
        //     $scope.activeWall = dummyCoursesService.findCourseById(courseId);
        // };
        //
        // $scope.getPostsInWall = function(courseId){
        //     return wallPostsService.getPostsInWall(courseId);
        // };
        //
        // $scope.isFavoriteOf = function (postId) {
        //     return wallPostsService.isFavoriteOf('user00@example.com', postId);
        // }
        //
        // $scope.getUser = function(email){
        //     return dummyUsersService.getDummyUser(email);
        // };
        //
        // $scope.wallPosts = wallPostsService.getWallPosts();

    }
    ]
);
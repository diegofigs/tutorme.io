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
angular.module('publicApp')
    .controller('WallCtrl', [
        '$scope',
        '$location',
        '$route',
        'auth',
        'mailbox',
        'wall',
        'courses',
        function ($scope, $location, $route, auth, mailbox, wall, courses) {

            $scope.currentSection = courses.section;

            $scope.wallPosts = function(sectionId){
                return wall.getPosts(sectionId);
            };

            // $scope.isFavoriteOf = function (postId) {
            //     return wallPostsService.isFavoriteOf('user00@example.com', postId);
            // }
            //
            // $scope.getUser = function(email){
            //     return dummyUsersService.getDummyUser(email);
            // };
            //
            // $scope.wallPosts = wallPostsService.getWallPosts();

        }]
);
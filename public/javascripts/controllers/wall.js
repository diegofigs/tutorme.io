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
        '$routeParams',
        'auth',
        'mailbox',
        'wall',
        'courses',
        function ($scope, $location, $routeParams, auth, mailbox, wall, courses) {

            var sectionId = $routeParams.section_id;
            console.log(sectionId);

            $scope.post = {};
            $scope.getPost = function(){
                if(!$scope.post.text){ return; }
                $scope.post.fromEmail = auth.currentUser().email;
                $scope.post.sectionId = sectionId;
                wall.postPost($scope.post).success(function(){
                    wall.getPosts(sectionId).then(function(posts){
                        wallPosts = posts.data;
                        $scope.wallPosts = wallPosts;
                    });
                    $scope.post = {};
                });
            };

            var wallPosts = null;
            wall.getPosts(sectionId).then(function(posts){
                wallPosts = posts.data;
                $scope.wallPosts = wallPosts;
                console.log(wallPosts);
                $scope.isFavoriteOf  = function(pid){
                    for(var i=0; i<wallPosts.length; i++){
                        if(wallPosts[i].id == pid){
                            return wallPosts[i].favoriteOf.includes(auth.currentUser().email)
                        }
                    }
                };
            });

            $scope.fav = {};
            $scope.toggleFavorite = function(pid){
                $scope.fav.pid = pid;
                $scope.fav.email = auth.currentUser().email;
                $scope.fav.current = wallPosts[getIndexOfPost(pid)].favoriteOf;
                if(!$scope.isFavoriteOf(pid)){
                    wall.makeFavorite($scope.fav).success(function(){
                        wall.getPosts(sectionId).then(function(posts){
                            wallPosts = posts.data;
                            $scope.wallPosts = wallPosts;
                            console.log(wallPosts);
                            $scope.isFavoriteOf  = function(pid){
                                for(var i=0; i<wallPosts.length; i++){
                                    if(wallPosts[i].id == pid){
                                        return wallPosts[i].favoriteOf.includes(auth.currentUser().email)
                                    }
                                }
                            };
                        });
                    });
                }
                else{
                    wall.removeFavorite($scope.fav).success(function(){
                        wall.getPosts(sectionId).then(function(posts){
                            wallPosts = posts.data;
                            $scope.wallPosts = wallPosts;
                            console.log(wallPosts);
                            $scope.isFavoriteOf  = function(pid){
                                for(var i=0; i<wallPosts.length; i++){
                                    if(wallPosts[i].id == pid){
                                        return wallPosts[i].favoriteOf.includes(auth.currentUser().email)
                                    }
                                }
                            };
                        });
                    });
                }
                $scope.fav = {};
            };

            var currSection = null;
            courses.getSectionById(sectionId).then(function(section){
                currSection = section.data;
                $scope.currentSection = currSection;
                console.log(currSection);
            });

            mailbox.getUserList().then(function(userList){
                var keys = Object.keys(userList.data);
                var values = keys.map(function(v) { return userList.data[v]; });
                $scope.userNamesArray = values;
                $scope.getUserName = function (email) {
                    return values[keys.indexOf(email)];
                }
            });

            function getIndexOfPost(pid){
                for(var i=0; i<wallPosts.length; i++){
                    if(wallPosts[i].id == pid){
                        return i;
                    }
                }
                return -1;
            }

        }]
);
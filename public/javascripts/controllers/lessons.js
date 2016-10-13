/**
 * Created by luisr on 10/10/2016.
 */
'use strict';
/**
 * @ngdoc function
 * @name publicApp.controller:LessonsTutorCtrl
 * @description
 * # LessonsTutorCtrl
 * Controller of the Lessons View of TutorMe.io
 */
angular.module('publicApp').controller('LessonsCtrl',
            function ($scope, auth, lessonsService, $sce) {


                $scope.lessons = lessonsService.getLessons();

                $scope.trustSrc = function(videoURL) {
                    return $sce.trustAsResourceUrl(videoURL);
                }

                $scope.insertLesson = function(){
                    //To be implemented in next phase
                };

                $scope.insertDocumentInLesson = function(){
                    //To be implemented in next phase
                };

                $scope.insertAssignmentInLesson = function(){
                    //To be implemented in next phase
                };


            }

    );
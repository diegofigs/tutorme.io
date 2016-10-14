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
                var lessons = lessonsService.getLessons();
                $scope.activeLesson=lessons[0];
                $scope.lessons = lessonsService.getLessons();
                $scope.getActiveLesson = function(){
                    return $scope.activeLesson;
                };
                $scope.setActiveLesson = function(name){
                    $scope.activeLesson = lessonsService.getLesson(name);
                };
                $scope.trustSrc = function(videoURL) {
                    return $sce.trustAsResourceUrl(videoURL);
                }
                $scope.modalDetails = function(assign){
                    $scope.assign = assign;
                    $('#modalDetails').openModal();
                };

                $scope.insertLesson = function(){
                    //To be implemented in next phase
                };

                $scope.insertDocumentInLesson = function(){
                    //To be implemented in next phase
                };

                $scope.getIframeSrc = function(src) {
                    return 'https://www.youtube.com/embed/' + src;
                };

                $scope.insertAssignmentInLesson = function(){
                    //To be implemented in next phase
                };

                $scope.$watch('lessons[0].open', function(isOpen){
                    if (isOpen) {
                        console.log('First lesson was opened');
                    }
                });


            }

    );
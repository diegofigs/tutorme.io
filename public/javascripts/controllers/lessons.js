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
angular.module('publicApp').controller('LessonsCtrl',['$scope',
    'auth',
    '$sce',
    'lessons',
    function ($scope, auth, $sce, lessons) {

        var lessns= null;


            lessons.getLessons().success(function(data){
                lessns = data;
                $scope.lessons = data;
                $scope.activeLesson=lessns[0];

                $scope.getActiveLesson = function(){
                    return $scope.activeLesson;
                };

                $scope.trustSrc = function(videoURL) {
                    return $sce.trustAsResourceUrl(videoURL);
                }
                $scope.modalDetails = function(assign){
                    $scope.assign = assign;
                    $('#modalDetails').openModal();
                };
                $scope.openModal = function(id){
                    $(id).openModal();
                };
                function expandCollapsible(id){
                    $(id).addClass("active");
                }
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

                $scope.$watch('lessns[0].open', function(isOpen){
                    if (isOpen) {
                        console.log('First lesson was opened');
                    }
                });

            });






        $scope.trustSrc = function(videoURL) {
            return $sce.trustAsResourceUrl(videoURL);
        }
        $scope.modalDetails = function(assign){
            $scope.assign = assign;
            $('#modalDetails').openModal();
        };
        $scope.openModal = function(id){
            $(id).openModal();
        };
        function expandCollapsible(id){
            $(id).addClass("active");
        }
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

        $scope.$watch('lessns[0].open', function(isOpen){
            if (isOpen) {
                console.log('First lesson was opened');
            }
        });



    }
]

    );
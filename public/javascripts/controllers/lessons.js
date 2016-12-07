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
    '$anchorScroll',
    '$location',
    '$routeParams',
    'courses',
    'comments',
    'mailbox',
    function ($scope, auth, $sce, lessons, $anchorScroll, $location, $routeParams,courses, comments, mailbox) {

        var lessns= null;
        var currentUser = null;
        var currentSection = null;
        var sec;
        var sId = null;
        var tempFile = null;

        lessons.getLessons($routeParams);
        lessons.getLessons($routeParams).success(function (data) {
            lessns = data;
            $scope.lessons = data;
            $scope.activeLesson = lessns[0];
            sId = $routeParams.sId;
            sec = $routeParams;
            $scope.sId = sId;

            $scope.getActiveLesson = function () {
                return $scope.activeLesson;
            };

            $scope.setActiveLesson = function (lesson) {
                $scope.activeLesson = lesson;
            };

            $scope.trustSrc = function (videoURL) {
                return $sce.trustAsResourceUrl(videoURL);
            }
            $scope.modalDetails = function (assign) {
                $scope.assign = assign;
                $('#modalDetails').openModal();
            };
            $scope.openModal = function (id) {
                $(id).openModal();
            };
            function expandCollapsible(id) {
                $(id).addClass("active");
            }

            $scope.insertDocumentInLesson = function () {
                //To be implemented in next phase
            };

            $scope.getIframeSrc = function (src) {
                return 'https://www.youtube.com/embed/' + src;
            };

            $scope.insertAssignmentInLesson = function () {
                //To be implemented in next phase
            };

            $scope.$watch('lessns[0].open', function (isOpen) {
                if (isOpen) {
                    console.log('First lesson was opened');
                }
            });

            currentUser = auth.currentUser();

            currentSection = courses.getCurrentSection();

            $scope.getUserType = function () {
                // console.log(currentUser);
                return currentUser.type;
            };

            $scope.isFavoriteOf  = function(cid, vid){
                for(var i=0; i<$scope.getActiveLesson().videos.length; i++){
                    if($scope.getActiveLesson().videos[i].id == vid){
                        for(var j=0; j<$scope.getActiveLesson().videos[i].comments.length; j++){
                            if($scope.getActiveLesson().videos[i].comments[j].id == cid){
                                return $scope.getActiveLesson().videos[i].comments[j].favoriteOf.includes(auth.currentUser().email)
                            }
                        }
                    }
                }
            };

        });

        $scope.comment = {};
        $scope.sendComment = function(videoId){
            if(!$scope.comment.text){ return; }
            $scope.comment.fromEmail = auth.currentUser().email;
            $scope.comment.videoId = videoId;
            var indexTemp = lessns.indexOf($scope.getActiveLesson());
            comments.postComment($scope.comment).success(function(){
                $scope.comment = {};
                lessons.getLessons($routeParams).success(function (data) {
                    lessns = data;
                    $scope.lessons = data;
                    $scope.activeLesson = lessns[indexTemp];
                    sId = $routeParams.sId;
                    sec = $routeParams;
                    $scope.sId = sId;

                    $scope.getActiveLesson = function () {
                        return $scope.activeLesson;
                    };

                    $scope.setActiveLesson = function (lesson) {
                        $scope.activeLesson = lesson;
                    };

                    $scope.trustSrc = function (videoURL) {
                        return $sce.trustAsResourceUrl(videoURL);
                    }
                    $scope.modalDetails = function (assign) {
                        $scope.assign = assign;
                        $('#modalDetails').openModal();
                    };
                    $scope.openModal = function (id) {
                        $(id).openModal();
                    };
                    function expandCollapsible(id) {
                        $(id).addClass("active");
                    }

                    $scope.insertDocumentInLesson = function () {
                        //To be implemented in next phase
                    };

                    $scope.getIframeSrc = function (src) {
                        return 'https://www.youtube.com/embed/' + src;
                    };

                    $scope.insertAssignmentInLesson = function () {
                        //To be implemented in next phase
                    };

                    $scope.$watch('lessns[0].open', function (isOpen) {
                        if (isOpen) {
                            console.log('First lesson was opened');
                        }
                    });

                    currentUser = auth.currentUser();

                    currentSection = courses.getCurrentSection();

                    $scope.getUserType = function () {
                        // console.log(currentUser);
                        return currentUser.type;
                    };

                    $scope.isFavoriteOf  = function(cid, vid){
                        for(var i=0; i<$scope.getActiveLesson().videos.length; i++){
                            if($scope.getActiveLesson().videos[i].id == vid){
                                for(var j=0; j<$scope.getActiveLesson().videos[i].comments.length; j++){
                                    if($scope.getActiveLesson().videos[i].comments[j].id == cid){
                                        return $scope.getActiveLesson().videos[i].comments[j].favoriteOf.includes(auth.currentUser().email)
                                    }
                                }
                            }
                        }
                    };

                });
            });
        };

        mailbox.getUserList().then(function(userList){
            var keys = Object.keys(userList.data);
            var values = keys.map(function(v) { return userList.data[v]; });
            $scope.userNamesArray = values;
            $scope.getUserName = function (email) {
                return values[keys.indexOf(email)];
            }
        });

        $scope.getDocument = function(did){
            lessons.getDocument(did, lid).success

        }

        $scope.tempFile= function (files) {
            tempFile = files[0]

        };


        $scope.addLesson=function(lName){
            if($scope.lName===null){ console.log('Estas mal'); return;}
            lessons.addLesson(sId,lName).success(function(data){
                console.log('muy bien');
                lessons.getLessons(sec);
                lessons.getLessons(sec).success(function (data) {
                    lessns = data;
                    $scope.lessons = data;
                    var temp = $scope.activeLesson;
                    $scope.activeLesson = $scope.lessons[0];
                    $scope.activeLesson = temp;
                    $scope.trustSrc = function (videoURL) {
                        return $sce.trustAsResourceUrl(videoURL);
                    }
                    $scope.modalDetails = function (assign) {
                        $scope.assign = assign;
                        $('#modalDetails').openModal();
                    };
                    $scope.openModal = function (id) {
                        $(id).openModal();
                    };
                    function expandCollapsible(id) {
                        $(id).addClass("active");
                    }

                    $scope.insertDocumentInLesson = function () {
                        //To be implemented in next phase
                    };

                    $scope.getIframeSrc = function (src) {
                        return 'https://www.youtube.com/embed/' + src;
                    };

                    $scope.insertAssignmentInLesson = function () {
                        //To be implemented in next phase
                    };

                    $scope.$watch('lessns[0].open', function (isOpen) {
                        if (isOpen) {
                            console.log('First lesson was opened');
                        }
                    });

                    currentUser = auth.currentUser();

                    currentSection = courses.getCurrentSection();

                    $scope.getUserType = function () {
                        // console.log(currentUser);
                        return currentUser.type;
                    }
                });
            });
        };

        $scope.addDocument = function(title, description){
            if(title===null||description===null||tempFile===null){console.log("file: " + tempFile); return;}
            var path;
            var fd = new FormData();
            fd.append("file",tempFile);
            tempFile=null;
            fd.append("title", title);
            fd.append("description",description);
            fd.append("lid",$scope.activeLesson.id);
            lessons.uploadDocument(fd).success(function(data){
                lessons.getLessons(sec);
                lessons.getLessons(sec).success(function (data) {
                    lessns = data;
                    $scope.lessons = data;
                    var temp = $scope.activeLesson;
                    $scope.activeLesson = $scope.lessons[0];
                    $scope.activeLesson = temp;
                    $scope.trustSrc = function (videoURL) {
                        return $sce.trustAsResourceUrl(videoURL);
                    }
                    $scope.modalDetails = function (assign) {
                        $scope.assign = assign;
                        $('#modalDetails').openModal();
                    };
                    $scope.openModal = function (id) {
                        $(id).openModal();
                    };
                    function expandCollapsible(id) {
                        $(id).addClass("active");
                    }

                    $scope.insertDocumentInLesson = function () {
                        //To be implemented in next phase
                    };

                    $scope.getIframeSrc = function (src) {
                        return 'https://www.youtube.com/embed/' + src;
                    };

                    $scope.insertAssignmentInLesson = function () {
                        //To be implemented in next phase
                    };

                    $scope.$watch('lessns[0].open', function (isOpen) {
                        if (isOpen) {
                            console.log('First lesson was opened');
                        }
                    });

                    currentUser = auth.currentUser();

                    currentSection = courses.getCurrentSection();

                    $scope.getUserType = function () {
                        // console.log(currentUser);
                        return currentUser.type;
                    }
                });
            });
        };

        $scope.addVideo=function(title, src){
            if(title===null||src===null){ console.log('Estas mal'); return;}
            console.log($scope.activeLesson);
            lessons.addVideo(title,src, $scope.activeLesson.id).success(function(data){
                console.log(sId);
                lessons.getLessons(sec);
                lessons.getLessons(sec).success(function (data) {
                    lessns = data;
                    $scope.lessons = data;
                    var temp = $scope.activeLesson;
                    $scope.activeLesson = $scope.lessons[0];
                    $scope.activeLesson = temp;
                    $scope.trustSrc = function (videoURL) {
                        return $sce.trustAsResourceUrl(videoURL);
                    }
                    $scope.modalDetails = function (assign) {
                        $scope.assign = assign;
                        $('#modalDetails').openModal();
                    };
                    $scope.openModal = function (id) {
                        $(id).openModal();
                    };
                    function expandCollapsible(id) {
                        $(id).addClass("active");
                    }

                    $scope.insertDocumentInLesson = function () {
                        //To be implemented in next phase
                    };

                    $scope.getIframeSrc = function (src) {
                        return 'https://www.youtube.com/embed/' + src;
                    };

                    $scope.insertAssignmentInLesson = function () {
                        //To be implemented in next phase
                    };

                    $scope.$watch('lessns[0].open', function (isOpen) {
                        if (isOpen) {
                            console.log('First lesson was opened');
                        }
                    });

                    currentUser = auth.currentUser();

                    currentSection = courses.getCurrentSection();

                    $scope.getUserType = function () {
                        // console.log(currentUser);
                        return currentUser.type;
                    }
                });
            });
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

        $scope.gotoAnchor = function(x) {
            console.log(x);
            // var newHash = x;
            // if ($location.hash() !== newHash) {
            //     // set the $location.hash to `newHash` and
            //     // $anchorScroll will automatically scroll to it
            //     $location.hash(x);
            // } else {
                // call $anchorScroll() explicitly,
                // since $location.hash hasn't changed
                $anchorScroll(x);
            //}
        };



    }
]

    );
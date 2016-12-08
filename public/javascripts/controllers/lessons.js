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
<<<<<<< 23e31b25a5a19e0663d3bfd0b55ebd8b16604719
    'comments',
    'mailbox',
    function ($scope, auth, $sce, lessons, $anchorScroll, $location, $routeParams,courses, comments, mailbox) {
=======
    '$route',
    function ($scope, auth, $sce, lessons, $anchorScroll, $location, $routeParams,courses,$route) {
>>>>>>> New Relation 'submissions'. grading system. assignment submission

        var lessns= null;
        var currentUser = null;
        var currentSection;
        var sec;
        var sId = null;
        var tempFile = null;
<<<<<<< 23e31b25a5a19e0663d3bfd0b55ebd8b16604719

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
=======
        $scope.dtd=null;
        $scope.subs=null;
        lessons.getLessons($routeParams);
        lessons.getLessons($routeParams).success(function (data) {
                    lessns = data[0];
                    $scope.lessons = data[0];
                    $scope.activeLesson = lessns[0];
                    $scope.courseTitle = data[1];
>>>>>>> New Relation 'submissions'. grading system. assignment submission
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
                    $scope.openModalD = function (id, dtd) {
                $(id).openModal();
                $scope.dtd=dtd;
            };
            $scope.openModalS = function (id, subs) {
                $(id).openModal();
                $scope.subs=subs;
            };
                    function expandCollapsible(id) {
                        $(id).addClass("active");
                    }

                    $scope.getIframeSrc = function (src) {
                        return 'https://www.youtube.com/embed/' + src;
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

        $scope.fav = {};
        $scope.toggleFavorite = function(cid, vid){
            $scope.fav.cid = cid;
            $scope.fav.vid = vid;
            $scope.fav.email = auth.currentUser().email;
            var vindex = getIndexOfVideo(vid);
            var indexTemp = lessns.indexOf($scope.getActiveLesson());
            $scope.fav.current = $scope.getActiveLesson().videos[vindex].comments[getIndexOfComment(vindex, cid)].favoriteOf;
            if(!$scope.isFavoriteOf(cid, vid)){
                comments.makeFavorite($scope.fav).success(function(){
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
            }
            else{
                comments.removeFavorite($scope.fav).success(function(){
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
            }
            $scope.fav = {};
        };

        function getIndexOfVideo(vid){
            for(var i=0; i<$scope.getActiveLesson().videos.length; i++){
                if($scope.getActiveLesson().videos[i].id == vid){
                    return i;
                }
            }
            return -1;
        }

        function getIndexOfComment(vindex, cid){
            for(var i=0; i<$scope.getActiveLesson().videos[vindex].comments.length; i++){
                if($scope.getActiveLesson().videos[vindex].comments[i].id == cid){
                    return i;
                }
            }
            return -1;
        }

        mailbox.getUserList().then(function(userList){
            var keys = Object.keys(userList.data);
            var values = keys.map(function(v) { return userList.data[v]; });
            $scope.userNamesArray = values;
            $scope.getUserName = function (email) {
                return values[keys.indexOf(email)];
            }
        });

            $scope.refresh = function () {
                $route.reload();

            };
        $scope.reload = function () {
            lessons.getLessons($routeParams).success(function (data) {
                lessns = data[0];
                $scope.lessons = data[0];
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
                $scope.openModalD = function (id, dtd) {
                    $(id).openModal();
                    $scope.dtd=dtd;
                };
                $scope.openModalS = function (id, subs) {
                    $(id).openModal();
                    $scope.subs=subs;
                };
                function expandCollapsible(id) {
                    $(id).addClass("active");
                }

                $scope.getIframeSrc = function (src) {
                    return 'https://www.youtube.com/embed/' + src;
                };

                $scope.$watch('lessns[0].open', function (isOpen) {
                    if (isOpen) {
                        console.log('First lesson was opened');
                    }
                });

                currentUser = auth.currentUser();


                $scope.getUserType = function () {
                    // console.log(currentUser);
                    return currentUser.type;
                }

            });

        };


        $scope.tempFile= function (files) {
            tempFile = files[0]

        };


        $scope.addLesson=function(lName){
            if($scope.lName===null){ console.log('Estas mal'); return;}
            lessons.addLesson(sId,lName).success(function(data){
                console.log('muy bien');
                // lessons.getLessons(sec);
                // lessons.getLessons(sec).success(function (data) {
                //     lessns = data;
                //     $scope.lessons = data;
                //     var temp = $scope.activeLesson;
                //     $scope.activeLesson = $scope.lessons[0];
                //     $scope.activeLesson = temp;
                //     $scope.trustSrc = function (videoURL) {
                //         return $sce.trustAsResourceUrl(videoURL);
                //     }
                //     $scope.modalDetails = function (assign) {
                //         $scope.assign = assign;
                //         $('#modalDetails').openModal();
                //     };
                //     $scope.openModal = function (id) {
                //         $(id).openModal();
                //     };
                //     $scope.openModalD = function (id, dtd) {
                //         $(id).openModal();
                //         $scope.dtd=dtd;
                //     };
                //     function expandCollapsible(id) {
                //         $(id).addClass("active");
                //     }
                //
                //     $scope.insertDocumentInLesson = function () {
                //         //To be implemented in next phase
                //     };
                //
                //     $scope.getIframeSrc = function (src) {
                //         return 'https://www.youtube.com/embed/' + src;
                //     };
                //
                //     $scope.insertAssignmentInLesson = function () {
                //         //To be implemented in next phase
                //     };
                //
                //     $scope.$watch('lessns[0].open', function (isOpen) {
                //         if (isOpen) {
                //             console.log('First lesson was opened');
                //         }
                //     });
                //
                //     currentUser = auth.currentUser();
                //
                //     currentSection = courses.getCurrentSection();
                //
                //     $scope.getUserType = function () {
                //         // console.log(currentUser);
                //         return currentUser.type;
                //     }
                // });
                $scope.reload();
            });
            
        };
        $scope.deleteLesson = function(lesson){
            lessons.deleteLesson(lesson.id).success(function (data){

                lessons.getLessons($routeParams).success(function (data) {
                    lessns = data[0];
                    $scope.lessons = data[0];
                    $scope.activeLesson = lessns[0];
                    $scope.courseTitle = data[1];
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
                    $scope.openModalD = function (id, dtd) {
                        $(id).openModal();
                        $scope.dtd=dtd;
                    };
                    $scope.openModalS = function (id, subs) {
                        $(id).openModal();
                        $scope.subs=subs;
                    };
                    function expandCollapsible(id) {
                        $(id).addClass("active");
                    }

                    $scope.getIframeSrc = function (src) {
                        return 'https://www.youtube.com/embed/' + src;
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

                });
                $scope.reload();
            });

        };

        $scope.addDocument = function(title, description){
            if(title===null||description===null||tempFile===null){console.log("file: " + tempFile); return;}
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
                    $scope.openModalD = function (id, dtd) {
                        $(id).openModal();
                        $scope.dtd=dtd;
                    };
                    $scope.openModalS = function (id, subs) {
                        $(id).openModal();
                        $scope.subs=subs;
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
                $scope.reload();
            });
        };
        $scope.deleteDocument = function(did){
            lessons.deleteDocument(did).success(function (data){

                lessons.getLessons($routeParams).success(function (data) {
                    lessns = data[0];
                    $scope.lessons = data[0];
                    $scope.activeLesson = lessns[0];
                    $scope.courseTitle = data[1];
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
                    $scope.openModalD = function (id, dtd) {
                        $(id).openModal();
                        $scope.dtd=dtd;
                    };
                    $scope.openModalS = function (id, subs) {
                        $(id).openModal();
                        $scope.subs=subs;
                    };
                    function expandCollapsible(id) {
                        $(id).addClass("active");
                    }

                    $scope.getIframeSrc = function (src) {
                        return 'https://www.youtube.com/embed/' + src;
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

                });
                $scope.reload();
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
                    $scope.openModalD = function (id, dtd) {
                        $(id).openModal();
                        $scope.dtd=dtd;
                    };
                    $scope.openModalS = function (id, subs) {
                        $(id).openModal();
                        $scope.subs=subs;
                    };
                    function expandCollapsible(id) {
                        $(id).addClass("active");
                    }

                    $scope.getIframeSrc = function (src) {
                        return 'https://www.youtube.com/embed/' + src;
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
                $route.reload();

            });
            
        };
        $scope.deleteVideo = function(vid){
            lessons.deleteVideo(vid).success(function (data){

                lessons.getLessons($routeParams).success(function (data) {
                    lessns = data[0];
                    $scope.lessons = data[0];
                    $scope.activeLesson = lessns[0];
                    $scope.courseTitle = data[1];
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
                    $scope.openModalD = function (id, dtd) {
                        $(id).openModal();
                        $scope.dtd=dtd;
                    };
                    $scope.openModalS = function (id, subs) {
                        $(id).openModal();
                        $scope.subs=subs;
                    };
                    function expandCollapsible(id) {
                        $(id).addClass("active");
                    }

                    $scope.getIframeSrc = function (src) {
                        return 'https://www.youtube.com/embed/' + src;
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

                });
                $scope.reload();
            });



        };


        $scope.addAssignment = function(title, description, date){
            if(title===null||description===null||tempFile===null||date===null){ return;}
            var path;
            var fd = new FormData();
            fd.append("file",tempFile);
            tempFile=null;
            fd.append("title", title);
            fd.append("description",description);
            fd.append("date", date);
            fd.append("lid",$scope.activeLesson.id);
            lessons.uploadAssignment(fd).success(function (data) {
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
                    $scope.openModalD = function (id, dtd) {
                        $(id).openModal();
                        $scope.dtd=dtd;
                    };
                    $scope.openModalS = function (id, subs) {
                        $(id).openModal();
                        $scope.subs=subs;
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
                $route.reload();

            });
            
        };
        $scope.deleteAssignment = function(aid){
            lessons.deleteAssignment(aid).success(function (data){
                lessons.getLessons(sec);
                lessons.getLessons($routeParams).success(function (data) {
                    lessns = data[0];
                    $scope.lessons = data[0];
                    $scope.activeLesson = lessns[0];
                    $scope.courseTitle = data[1];
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
                    $scope.openModalD = function (id, dtd) {
                        $(id).openModal();
                        $scope.dtd=dtd;
                    };
                    $scope.openModalS = function (id, subs) {
                        $(id).openModal();
                        $scope.subs=subs;
                    };
                    function expandCollapsible(id) {
                        $(id).addClass("active");
                    }

                    $scope.getIframeSrc = function (src) {
                        return 'https://www.youtube.com/embed/' + src;
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

                });
                $scope.reload();
            });

            

        };

        $scope.addSubmission = function(aid){
            var fd = new FormData();
            fd.append("aid", aid);
            fd.append("stid", auth.currentUser().id);
            fd.append("file",tempFile);
            tempFile = null;
            lessons.uploadSubmission(fd, aid).success(function(data){
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
                    $scope.openModalD = function (id, dtd) {
                        $(id).openModal();
                        $scope.dtd=dtd;
                    };
                    $scope.openModalS = function (id, subs) {
                        $(id).openModal();
                        $scope.subs=subs;
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
                $scope.reload();
            });
            
        };

        $scope.submitGrade = function(grade, subid){
            lessons.submitGrade(grade, subid).success(function (data) {
                // lessons.getLessons(sec).success(function (data) {
                //     lessns = data;
                //     $scope.lessons = data;
                //     var temp = $scope.activeLesson;
                //     $scope.activeLesson = $scope.lessons[0];
                //     $scope.activeLesson = temp;
                //     $scope.trustSrc = function (videoURL) {
                //         return $sce.trustAsResourceUrl(videoURL);
                //     }
                //     $scope.modalDetails = function (assign) {
                //         $scope.assign = assign;
                //         $('#modalDetails').openModal();
                //     };
                //     $scope.openModal = function (id) {
                //         $(id).openModal();
                //     };
                //     $scope.openModalD = function (id, dtd) {
                //         $(id).openModal();
                //         $scope.dtd=dtd;
                //     };
                //     function expandCollapsible(id) {
                //         $(id).addClass("active");
                //     }
                //
                //     $scope.insertDocumentInLesson = function () {
                //         //To be implemented in next phase
                //     };
                //
                //     $scope.getIframeSrc = function (src) {
                //         return 'https://www.youtube.com/embed/' + src;
                //     };
                //
                //     $scope.insertAssignmentInLesson = function () {
                //         //To be implemented in next phase
                //     };
                //
                //     $scope.$watch('lessns[0].open', function (isOpen) {
                //         if (isOpen) {
                //             console.log('First lesson was opened');
                //         }
                //     });
                //
                //     currentUser = auth.currentUser();
                //
                //     currentSection = courses.getCurrentSection();
                //
                //     $scope.getUserType = function () {
                //         // console.log(currentUser);
                //         return currentUser.type;
                //     }
                // });
                $scope.reload();
            });
            
        };

        $scope.checkDeadline = function (deadline) {
            var today = new Date();
            var date = new Date(deadline);
            var bl= today<=date;
            return bl;

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
        $scope.openModalD = function (id, dtd) {
            $(id).openModal();
            $scope.dtd=dtd;
        };
        $scope.openModalS = function (id, subs) {
            $(id).openModal();
            $scope.subs=subs;
        };
        function expandCollapsible(id){
            $(id).addClass("active");
        }
        $scope.getIframeSrc = function(src) {
            return 'https://www.youtube.com/embed/' + src;
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
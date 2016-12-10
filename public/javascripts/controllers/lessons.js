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
angular.module('publicApp')
    .run(['$anchorScroll', function($anchorScroll) {
    $anchorScroll.yOffset = 120;   // always scroll by 50 extra pixels
}])
    .controller('LessonsCtrl',['$scope',
    'auth',
    '$sce',
    'lessons',
    '$anchorScroll',
    '$location',
    '$routeParams',
    'courses',
    'comments',
    'mailbox',
    '$route',
    '$timeout',
    function ($scope, auth, $sce, lessons, $anchorScroll, $location, $routeParams,courses, comments, mailbox,$route,$timeout) {
        $scope.dtd = null;
        $scope.subs = null;
        var lessns = null;
        var currentUser = null;
        var currentSection = null;
        var sec;
        var sId = null;
        var tempFile = null;
        var submissions=null;

        lessons.getLessons($routeParams);
        lessons.getLessons($routeParams).success(function (data) {
            lessns = data[0];
            $scope.lessons = data[0];
            $scope.activeLesson = lessns[0];
            $scope.courseTitle = data[1];
            sId = $routeParams.sId;
            sec = $routeParams;
            $scope.sId = sId;
            console.log(lessns[0]);
            $scope.refresh = function () {
                $route.reload();

            };

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

            $scope.isFavoriteOf = function (cid, vid) {
                for (var i = 0; i < $scope.getActiveLesson().videos.length; i++) {
                    if ($scope.getActiveLesson().videos[i].id == vid) {
                        for (var j = 0; j < $scope.getActiveLesson().videos[i].comments.length; j++) {
                            if ($scope.getActiveLesson().videos[i].comments[j].id == cid) {
                                return $scope.getActiveLesson().videos[i].comments[j].favoriteOf.includes(auth.currentUser().email)
                            }
                        }
                    }
                }
            };

        });

        $scope.comment = {};
        $scope.sendComment = function (videoId) {
            if (!$scope.comment.text) {
                return;
            }
            $scope.comment.fromEmail = auth.currentUser().email;
            $scope.comment.videoId = videoId;
            var indexTemp = lessns.indexOf($scope.getActiveLesson());
            comments.postComment($scope.comment).success(function () {
                $scope.comment = {};

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
                        $scope.dtd = dtd;
                    };
                    $scope.openModalS = function (id, subs) {
                        $(id).openModal();
                        $scope.subs = subs;
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
                    $scope.isFavoriteOf = function (cid, vid) {
                        for (var i = 0; i < $scope.getActiveLesson().videos.length; i++) {
                            if ($scope.getActiveLesson().videos[i].id == vid) {
                                for (var j = 0; j < $scope.getActiveLesson().videos[i].comments.length; j++) {
                                    if ($scope.getActiveLesson().videos[i].comments[j].id == cid) {
                                        return $scope.getActiveLesson().videos[i].comments[j].favoriteOf.includes(auth.currentUser().email)
                                    }
                                }
                            }
                        }
                    };

                });
                $scope.reload();//refresh();
            });
            //refresh();
        };

        $scope.fav = {};
        $scope.toggleFavorite = function (cid, vid) {
            $scope.fav.cid = cid;
            $scope.fav.vid = vid;
            $scope.fav.email = auth.currentUser().email;
            var vindex = getIndexOfVideo(vid);
            var indexTemp = lessns.indexOf($scope.getActiveLesson());
            $scope.fav.current = $scope.getActiveLesson().videos[vindex].comments[getIndexOfComment(vindex, cid)].favoriteOf;
            if (!$scope.isFavoriteOf(cid, vid)) {
                comments.makeFavorite($scope.fav).success(function () {
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
                            $scope.dtd = dtd;
                        };
                        $scope.openModalS = function (id, subs) {
                            $(id).openModal();
                            $scope.subs = subs;
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
                        $scope.isFavoriteOf = function (cid, vid) {
                            for (var i = 0; i < $scope.getActiveLesson().videos.length; i++) {
                                if ($scope.getActiveLesson().videos[i].id == vid) {
                                    for (var j = 0; j < $scope.getActiveLesson().videos[i].comments.length; j++) {
                                        if ($scope.getActiveLesson().videos[i].comments[j].id == cid) {
                                            return $scope.getActiveLesson().videos[i].comments[j].favoriteOf.includes(auth.currentUser().email)
                                        }
                                    }
                                }
                            }
                        };

                    });
                    $scope.reload();//refresh();
                });
            }
            else {
                comments.removeFavorite($scope.fav).success(function () {
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
                            $scope.dtd = dtd;
                        };
                        $scope.openModalS = function (id, subs) {
                            $(id).openModal();
                            $scope.subs = subs;
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
                        $scope.isFavoriteOf = function (cid, vid) {
                            for (var i = 0; i < $scope.getActiveLesson().videos.length; i++) {
                                if ($scope.getActiveLesson().videos[i].id == vid) {
                                    for (var j = 0; j < $scope.getActiveLesson().videos[i].comments.length; j++) {
                                        if ($scope.getActiveLesson().videos[i].comments[j].id == cid) {
                                            return $scope.getActiveLesson().videos[i].comments[j].favoriteOf.includes(auth.currentUser().email)
                                        }
                                    }
                                }
                            }
                        };

                    });
                    $scope.reload();//refresh();
                });
            }
            $scope.fav = {};
        };

        function getIndexOfVideo(vid) {
            for (var i = 0; i < $scope.getActiveLesson().videos.length; i++) {
                if ($scope.getActiveLesson().videos[i].id == vid) {
                    return i;
                }
            }
            return -1;
        }

        function getIndexOfComment(vindex, cid) {
            for (var i = 0; i < $scope.getActiveLesson().videos[vindex].comments.length; i++) {
                if ($scope.getActiveLesson().videos[vindex].comments[i].id == cid) {
                    return i;
                }
            }
            return -1;
        }

        mailbox.getUserList().then(function (userList) {
            var keys = Object.keys(userList.data);
            var values = keys.map(function (v) {
                return userList.data[v];
            });
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
                    $scope.dtd = dtd;
                };
                $scope.openModalS = function (id, subs) {
                    $(id).openModal();
                    $scope.subs = subs;
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
                $scope.isFavoriteOf = function (cid, vid) {
                    for (var i = 0; i < $scope.getActiveLesson().videos.length; i++) {
                        if ($scope.getActiveLesson().videos[i].id == vid) {
                            for (var j = 0; j < $scope.getActiveLesson().videos[i].comments.length; j++) {
                                if ($scope.getActiveLesson().videos[i].comments[j].id == cid) {
                                    return $scope.getActiveLesson().videos[i].comments[j].favoriteOf.includes(auth.currentUser().email)
                                }
                            }
                        }
                    }
                };

            });


        };


        $scope.tempFile = function (files) {
            tempFile = files[0]
0
        };


        $scope.addLesson = function (lName) {
            if ($scope.lName === null) {
                console.log('Estas mal');
                return;
            }
            lessons.addLesson(sId, lName).success(function (data) {
                console.log('muy bien');
                $scope.reload();
            });

        };
        $scope.deleteLesson = function (lesson) {
            lessons.deleteLesson(lesson.id).success(function (data) {

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
                        $scope.dtd = dtd;
                    };
                    $scope.openModalS = function (id, subs) {
                        $(id).openModal();
                        $scope.subs = subs;
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
                    $scope.isFavoriteOf = function (cid, vid) {
                        for (var i = 0; i < $scope.getActiveLesson().videos.length; i++) {
                            if ($scope.getActiveLesson().videos[i].id == vid) {
                                for (var j = 0; j < $scope.getActiveLesson().videos[i].comments.length; j++) {
                                    if ($scope.getActiveLesson().videos[i].comments[j].id == cid) {
                                        return $scope.getActiveLesson().videos[i].comments[j].favoriteOf.includes(auth.currentUser().email)
                                    }
                                }
                            }
                        }
                    };

                });
                $scope.reload();
            });

        };

        $scope.addDocument = function (title, description) {
            if (title === null || description === null || tempFile === null) {
                console.log("file: " + tempFile);
                return;
            }
            var fd = new FormData();
            fd.append("file", tempFile);
            tempFile = null;
            fd.append("title", title);
            fd.append("description", description);
            fd.append("lid", $scope.activeLesson.id);
            lessons.uploadDocument(fd).success(function (data) {
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
                        $scope.dtd = dtd;
                    };
                    $scope.openModalS = function (id, subs) {
                        $(id).openModal();
                        $scope.subs = subs;
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
                    $scope.isFavoriteOf = function (cid, vid) {
                        for (var i = 0; i < $scope.getActiveLesson().videos.length; i++) {
                            if ($scope.getActiveLesson().videos[i].id == vid) {
                                for (var j = 0; j < $scope.getActiveLesson().videos[i].comments.length; j++) {
                                    if ($scope.getActiveLesson().videos[i].comments[j].id == cid) {
                                        return $scope.getActiveLesson().videos[i].comments[j].favoriteOf.includes(auth.currentUser().email)
                                    }
                                }
                            }
                        }
                    };

                });
                $scope.reload();//refresh();
            });
        };
        $scope.deleteDocument = function (did) {
            lessons.deleteDocument(did).success(function (data) {

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
                        $scope.dtd = dtd;
                    };
                    $scope.openModalS = function (id, subs) {
                        $(id).openModal();
                        $scope.subs = subs;
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

        $scope.addVideo = function (title, src) {
            if (title === null || src === null) {
                console.log('Estas mal');
                return;
            }

            var s ="https://youtu.be/";
            var s1 ="https://www.youtube.com/watch?v=";
            if(src.includes(s))
                src = src.substring(s.length);
            else if (src.includes(s1))
                src = src.substring(s1.length);
            else return;


            console.log($scope.activeLesson);
            lessons.addVideo(title, src, $scope.activeLesson.id).success(function (data) {
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
                        $scope.dtd = dtd;
                    };
                    $scope.openModalS = function (id, subs) {
                        $(id).openModal();
                        $scope.subs = subs;
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
        $scope.deleteVideo = function (vid) {
            lessons.deleteVideo(vid).success(function (data) {

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
                        $scope.dtd = dtd;
                    };
                    $scope.openModalS = function (id, subs) {
                        $(id).openModal();
                        $scope.subs = subs;
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


        $scope.addAssignment = function (title, description, date) {
            if (title === null || description === null || tempFile === null || date === null) {
                return;
            }
            var path;
            var fd = new FormData();
            fd.append("file", tempFile);
            tempFile = null;
            fd.append("title", title);
            fd.append("description", description);
            fd.append("date", date);
            fd.append("lid", $scope.activeLesson.id);
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
                        $scope.dtd = dtd;
                    };
                    $scope.openModalS = function (id, subs) {
                        $(id).openModal();
                        $scope.subs = subs;
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
        $scope.deleteAssignment = function (aid) {
            lessons.deleteAssignment(aid).success(function (data) {
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
                        $scope.dtd = dtd;
                    };
                    $scope.openModalS = function (id, subs) {
                        $(id).openModal();
                        $scope.subs = subs;
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

        $scope.addSubmission = function (aid) {
            var fd = new FormData();
            fd.append("aid", aid);
            fd.append("stid", auth.currentUser().id);
            fd.append("file", tempFile);
            tempFile = null;
            lessons.uploadSubmission(fd, aid).success(function (data) {
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
                        $scope.dtd = dtd;
                    };
                    $scope.openModalS = function (id, subs) {
                        $(id).openModal();
                        $scope.subs = subs;
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
                $scope.reload();
            });

        };

        $scope.submitGrade = function (grade, subid) {
            lessons.submitGrade(grade, subid).success(function (data) {
            });

        };

        $scope.checkDeadline = function (deadline) {
            var today = new Date();
            var date = new Date(deadline);
            var bl = today <= date;
            return bl;

        };


        $scope.openModalD = function (id, dtd) {
            $(id).openModal();
            $scope.dtd = dtd;
        };
        $scope.openModalS = function (id, subs) {
            $(id).openModal();
            $scope.subs = subs;
        };

        $scope.checkSubmission = function (aid) {
            var stid = currentUser.id;
            var i =0;
            var assign =assignment(aid);
            return !(getSubmission(assign, stid)===null);



        };

        var getSubmission = function (assign, stid) {
            var i =0;
            for(i=0; i<assign.submissions.length;i++){
                if(assign.submissions[i].stID===stid)
                    return assign.submissions[i];
            }
            return null;

        };

        $scope.gotoAnchor = function(x) {
            console.log(x);
            $anchorScroll(x);
        };


        $scope.getGrade = function(assign){
            return getSubmission(assign, currentUser.id).grade;
        }

        var assignment = function (aid) {
            var i=0;
            for( i =0; i< $scope.activeLesson.assignments.length; i++){
                if($scope.activeLesson.assignments[i].id===aid)
                    return $scope.activeLesson.assignments[i];
            }
            return null;

        }

        $scope.isGraded = function (aid) {
            var submission = getSubmission(assignment(aid), currentUser.id)
            return (submission.grade>-1);
        }

    }]);

'use strict';

/**
 * @ngdoc overview
 * @name publicApp
 * @description
 * # publicApp
 *
 * Main module of the application.
 */

angular.module('publicApp', [
    'ngRoute'
])
    .config([
        '$routeProvider',
        '$locationProvider',
        function ($routeProvider, $locationProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'assets/views/main.html',
                controller: 'AuthCtrl',
                controllerAs: 'main'
            })
            .when('/about', {
                templateUrl: 'assets/views/about.html',
                controller: 'AboutCtrl',
                controllerAs: 'about'
            })
            .when('/login', {
                templateUrl: 'assets/views/login.html',
                controller: 'AuthCtrl',
                controllerAs: 'login'
            })
            .when('/register', {
                templateUrl: 'assets/views/register.html',
                controller: 'AuthCtrl',
                controllerAs: 'register'
            })
            .when('/home', {
                templateUrl: 'assets/views/home.html',
                controller: 'HomeCtrl',
                controllerAs: 'home',
                resolve: {
                    coursesPromise: ['courses', 'auth', function(courses, auth){
                        if(auth.currentUser().type == 0){
                            return courses.getTutorCourses(auth.currentUser().id);
                        }
                        else{
                            return courses.getStudentCourses(auth.currentUser().id);
                        }
                    }]
                }
            })
            .when('/wall/:section_id', {
                templateUrl: 'assets/views/wall.html',
                controller: 'WallCtrl',
                controllerAs: 'wall',
            })
            .when('/mailbox', {
                templateUrl: 'assets/views/mailbox.html',
                controller: 'MailboxCtrl',
                controllerAs: 'mailbox',
            })
            .when('/lessons/:sId', {
                templateUrl: 'assets/views/lessons.html',
                controller: 'LessonsCtrl',
                controllerAs: 'Lessons',
            })
            .when('/profile', {
                templateUrl: 'assets/views/profile.html',
                controller: 'ProfileCtrl',
                controllerAs: 'profile',
            })
            .when('/explore', {
                templateUrl: 'assets/views/explore.html',
                controller: 'ExploreCtrl',
                controllerAs: 'explore',
            })
            .when('/sections', {
                templateUrl: 'assets/views/course.html',
                controller: 'CourseCtrl',
                controllerAs: 'course',
            })
            .otherwise({
                redirectTo: '/'
            });
        $locationProvider.html5Mode(true);
    }])
    .factory('auth', ['$http', '$window', function($http, $window) {
        var auth = {};
        auth.isLoggedIn = function(){
            if(localStorage.getObject('user')){
                return true;
            }
            else{
                return false;
            }
        };
        auth.currentUser = function(){
            if(auth.isLoggedIn()){
                return localStorage.getObject('user');
            }
            return null;
        };
        auth.register = function(user){
            return $http.post('/register', user).success(function(data){
                localStorage.setObject('user', data);
            });
        };
        auth.login = function(user){
            return $http.post('/login', user).success(function(data){
                localStorage.setObject('user', data);
            });
        };
        auth.logout = function(){
            localStorage.setObject('user', null);
            localStorage.setObject('courses', null);
            localStorage.setObject('course', null);
            localStorage.setObject('section', null);
        };
        return auth;
    }])
    .factory('courses', ['$http', function ($http) {
        var courses = {};
        courses.getCurrentCourses = function(){
            if(localStorage.getObject('courses')){
                return localStorage.getObject('courses');
            }
            return null;
        };
        courses.getCurrentCourse = function(){
            if(localStorage.getObject('course')){
                return localStorage.getObject('course');
            }
            return null;
        };
        courses.getCurrentSection = function(){
            if(localStorage.getObject('section')){
                return localStorage.getObject('section');
            }
            return null;
        };
        courses.getAvailableSections = function(){
            if(localStorage.getObject('new_courses')){
                return localStorage.getObject('new_courses');
            }
            return null;
        };
        courses.getCourses = function(id){
            return $http.get('/explore/'+ id).success(function(data){
                localStorage.setObject('new_courses', data);
            });
        };
        courses.getTutorCourses = function(id){
            return $http.get('/tutors/' + id + '/courses').success(function(data){
                localStorage.setObject('courses', data);
                localStorage.setObject('course', null);
                localStorage.setObject('section', null);
            });
        };
        courses.getStudentCourses = function(id){
            return $http.get('/students/' + id + '/courses').success(function(data){
                localStorage.setObject('courses', data);
                localStorage.setObject('course', null);
                localStorage.setObject('section', null);
            });
        };
        courses.getSections = function(id){
            return $http.get('/courses/' + id).success(function(data){
                localStorage.setObject('courses', data[0]);
                localStorage.setObject('course', data);
                localStorage.setObject('section', null);
            });
        };
        courses.getSection = function(id){
            return $http.get('/sections/' + id).success(function(data){
                localStorage.setObject('section', data);
            });
        };
        courses.getSectionById = function(sectionId){
            return $http.get('/sections/' + sectionId).success(function(data){
                courses.section = data;
            });
        };
        return courses;
    }])
    .factory('mailbox', ['$http', '$window', 'auth', function($http, $window, auth) {
        var mailbox = {};
        mailbox.getUserList = function(){
            return $http.get('/mailbox/users').success(function(data){
                mailbox.userList = data;
            });
        };
        mailbox.getMessages = function(){
            return $http.get('/mailbox/' + auth.currentUser().email).success(function (data){
                mailbox.messageList = data;
            });
        };
        mailbox.sendMessage = function(message){
            console.log(message);
            return $http.post('/message', message).success(function(data){
                console.log("Message Posted");
            });
        };
        return mailbox;
    }])
    .factory('wall', ['$http', '$window', 'auth', function($http, $window, auth) {
        var wall = {};
        wall.getPosts = function(sectionId){
            return $http.get('/wall/get/' + sectionId).success(function (data){
                wall.messageList = data;
            });
        };
        wall.postPost = function(post){
            console.log(post);
            return $http.post('/wallpost', post).success(function(data){
                console.log("Post Posted");
            });
        };
        wall.makeFavorite = function(fav){
            console.log(fav);
            return $http.post('/makefav', fav).success(function(data){
                console.log("Made Fav");
            });
        };
        wall.removeFavorite = function(fav){
            console.log(fav);
            return $http.post('/rmfav', fav).success(function(data){
                console.log("Removed Fav");
            });
        };
        return wall;
    }])
    .factory('comments', ['$http', '$window', 'auth', function($http, $window, auth) {
        var comments = {};
        comments.postComment = function(comment){
            console.log(comment);
            return $http.post('/comment', comment).success(function(data){
                console.log("Comment Posted");
            });
        };
        comments.makeFavorite = function(fav){
            console.log(fav);
            return $http.post('/makefavcomm', fav).success(function(data){
                console.log("Made Fav");
            });
        };
        comments.removeFavorite = function(fav){
            console.log(fav);
            return $http.post('/rmfavcomm', fav).success(function(data){
                console.log("Removed Fav");
            });
        };
        return comments;
    }])
    .factory('lessons', ['$http', '$window', function($http, $window){

        var lessons={};
        var lessonlist = {};


        lessons.getLessons = function(id){
            console.log(id);
            return $http.get('/lessons/'+id.sId);
        };

        lessons.addLesson = function (sectionId, name) {
            return $http.post('/addlesson/'+sectionId+'/'+name);

        };

        lessons.deleteLesson = function(id){
            return $http.delete('/deletelesson/'+id);
        };

        lessons.addVideo = function (title, src, lid) {
            return $http.post('/addvideo/'+title+'/'+src+'/'+lid);

        };
        lessons.deleteVideo = function(vid){
            return $http.delete('/deletevideo/'+vid);
        };

        lessons.uploadDocument = function(fd){
            return $http.post('/uploaddocument',fd,{
                withCredentials: true,
                headers: {'Content-Type': undefined },
                transformRequest: angular.identity
                });
        };

        lessons.deleteDocument = function(did){
            return $http.delete('/deletedocument/'+did);
        };

        lessons.uploadAssignment = function(fd){
            return $http.post('/uploadassignment',fd,{
                withCredentials: true,
                headers: {'Content-Type': undefined },
                transformRequest: angular.identity
            });
        };

        lessons.deleteAssignment = function(aid){
            return $http.delete('/deleteassignment/'+aid);
        };

        lessons.uploadSubmission = function(fd, aid){
            return $http.post('/uploadsubmission',fd,{
                withCredentials: true,
                headers: {'Content-Type': undefined },
                transformRequest: angular.identity
            });
        };

        lessons.submitGrade = function (grade, subid) {
            return $http.post('/submitgrade/'+grade+'/'+subid);

        }








        return lessons;

    }]);

Storage.prototype.setObject = function(key, value) {
    this.setItem(key, JSON.stringify(value));
};

Storage.prototype.getObject = function(key) {
    var value = this.getItem(key);
    return value && JSON.parse(value);
};


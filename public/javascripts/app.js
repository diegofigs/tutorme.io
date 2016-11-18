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
            .when('/wall/:sectionId?', {
                templateUrl: 'assets/views/wall.html',
                controller: 'WallCtrl',
                controllerAs: 'wall',
            })
            .when('/mailbox', {
                templateUrl: 'assets/views/mailbox.html',
                controller: 'MailboxCtrl',
                controllerAs: 'mailbox',
            })
            .when('/lessons', {
                templateUrl: 'assets/views/lessons.html',
                controller: 'LessonsCtrl',
                controllerAs: 'Lessons',
            })
            .when('/profile', {
                templateUrl: 'assets/views/profile.html',
                controller: 'ProfileCtrl',
                controllerAs: 'profile',
            })
            .when('/courses', {
                templateUrl: 'assets/views/courses.html',
                controller: 'CourseCtrl',
                controllerAs: 'course',
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
            if(auth.user != null){
                return true;
            }
            else{
                return false;
            }
        };
        auth.currentUser = function(){
            if(auth.isLoggedIn()){
                return auth.user;
            }
            return null;
        };
        auth.register = function(user){
            return $http.post('/register', user).success(function(data){
                auth.user = data;
            });
        };
        auth.login = function(user){
            return $http.post('/login', user).success(function(data){
                auth.user = data;
            });
        };
        auth.logout = function(){
            auth.user = null;
        };
        return auth;
    }])
    .factory('courses', ['$http', function ($http) {
        var courses = {};
        courses.getCurrentCourse = function(){
            if(courses.course != null){
                return courses.course;
            }
            return null;
        };
        courses.getCurrentSection = function(){
            if(courses.section != null){
                return courses.section;
            }
            return null;
        };
        courses.getCourses = function(){
            return $http.get('/courses').success(function(data){
                courses.courses = data;
            });
        };
        courses.getTutorCourses = function(id){
            return $http.get('/tutors/' + id + '/courses').success(function(data){
                courses.courses = data;
                courses.course = null;
                courses.section = null;
            });
        };
        courses.getStudentCourses = function(id){
            return $http.get('/students/' + id + '/courses').success(function(data){
                courses.courses = data;
                courses.course = null;
                courses.section = null;
            });
        };
        courses.getSections = function(course){
            return $http.get('/courses/'+ course.id).success(function(data){
                courses.course = data[0];
                courses.sections = data;
                courses.section = null;
            });
        };
        courses.getSection = function(section){
            return $http.get('/sections/' + section.id).success(function(data){
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
            })
        }
        return mailbox;
    }])
    .factory('wallPost', ['$http', '$window', 'auth', function($http, $window, auth) {
        var wallPost = {};
        wallPost.getPosts = function(sectionId){
            return $http.get('/wall/get/' + sectionId).success(function (data){
                wallPost.messageList = data;
            })
        };
        return wallPost;
    }])
    .factory('lessons', ['$http', '$window', function($http, $window){

        var lessons={};
        var lessonlist = {};


        lessons.getLessons = function(){
            return $http.get('/lessons/lessonlist');


        };



        lessons.getLesson = function(name){
            for(var i =0;i<lessons.length;i++){
                if(lessons[i].name===name){
                    return lessons[i];
                }
            }
            return null;
        };



        return lessons;

    }]);


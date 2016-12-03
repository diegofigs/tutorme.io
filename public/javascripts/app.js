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
        courses.getCourses = function(){
            return $http.get('/courses').success(function(data){
                localStorage.setObject('courses', data);
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
        courses.getSections = function(course){
            return $http.get('/courses/'+ course.id).success(function(data){
                localStorage.setObject('courses', data[0]);
                localStorage.setObject('course', data);
                localStorage.setObject('section', null);
            });
        };
        courses.getSection = function(section){
            return $http.get('/sections/' + section.id).success(function(data){
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
            })
        }
        return mailbox;
    }])
    .factory('wall', ['$http', '$window', 'auth', function($http, $window, auth) {
        var wall = {};
        wall.getPosts = function(sectionId){
            return $http.get('/wall/get/' + sectionId).success(function (data){
                wall.messageList = data;
            })
        };
        return wall;
    }])
    .factory('lessons', ['$http', '$window', function($http, $window){

        var lessons={};
        var lessonlist = {};


        lessons.getLessons = function(id){
            console.log(id);
            return $http.get('/lessons/'+id.sId);
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

Storage.prototype.setObject = function(key, value) {
    this.setItem(key, JSON.stringify(value));
};

Storage.prototype.getObject = function(key) {
    var value = this.getItem(key);
    return value && JSON.parse(value);
};


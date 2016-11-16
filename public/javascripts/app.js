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
            })
            .when('/wall', {
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
    .factory('courses', ['$http', '$window', 'auth', function ($http, $window, auth) {
        var courses = {};
        courses.getSections = function(){
            return $http.get('/sections').success(function(data){
                courses.sections = data;
            });
        };
        courses.getSection = function(id){
            return $http.get('/sections/' + id).success(function(data){
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
    }]);


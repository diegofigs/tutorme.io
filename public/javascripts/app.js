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
        };
        auth.register = function(user){
            return $http.post('/register', user).success(function(data){
                auth.user = data.user;
            });
        };
        auth.login = function(user){
            return $http.post('/login', user).success(function(data){
                auth.user = data.user;
            });
        };
        auth.logout = function(){
            auth.user = null;
        };
        return auth;
    }]);


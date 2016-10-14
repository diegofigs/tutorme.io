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
        auth.saveToken = function(token) {
            $window.localStorage['tutorme-token'] = token;
        };
        auth.getToken = function() {
            return $window.localStorage['tutorme-token'];
        }
        auth.isLoggedIn = function(){
            var token = auth.getToken();
            if(token){
                var payload = JSON.parse($window.atob(token.split('.')[1]));
                return payload.exp > Date.now() / 1000;
            } else {
                return false;
            }
        };
        auth.currentUser = function(){
            if(auth.isLoggedIn()){
                var token = auth.getToken();
                var payload = JSON.parse($window.atob(token.split('.')[1]));
                return payload.sub;
            }
        };
        auth.register = function(user){
            return $http.post('/register', user).success(function(data){
                console.log(data);
                auth.saveToken(data.token);
            });
        };
        auth.login = function(user){
            return $http.post('/login', user).success(function(data){
                auth.saveToken(data.token);
            });
        };
        auth.logout = function(){
            $window.localStorage.removeItem('tutorme-token');
        }
        return auth;
    }]);


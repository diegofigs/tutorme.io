'use strict';

/**
 * @ngdoc function
 * @name publicApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the publicApp
 */
angular.module('publicApp')
    .controller('AuthCtrl', [
        '$scope',
        '$route',
        '$location',
        '$routeParams',
        'auth',
        function ($scope, $route, $location, $routeParams, auth) {
            $scope.user = {};
            $scope.createUser = function(){
                if(!$scope.user.email || !$scope.user.password){ return; }
                auth.register($scope.user).success(function(data){
                    $scope.user = {};
                    $location.path('/home');
                });
            };
            $scope.loginUser = function(){
                if(!$scope.user.email || !$scope.user.password){ return; }
                auth.login($scope.user).success(function(data){
                    $scope.user = {};
                    $location.path('/home');
                });
            }
        }
    ]
);

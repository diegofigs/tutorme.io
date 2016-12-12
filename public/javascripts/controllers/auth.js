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
        'mailbox',
        function ($scope, $route, $location, $routeParams, auth, mailbox) {
            $scope.user = {};
            $scope.userExists = false;
            $scope.createUser = function(){
                if(!$scope.user.email || !$scope.user.password || !$scope.user.firstname || !$scope.user.lastname){ return; }
                mailbox.getUserList().then(function(userList){
                    $scope.userExists = false;
                    var keys = Object.keys(userList.data);
                    if(keys.indexOf($scope.user.email)!=-1){
                        $scope.userExists = true;
                    }
                    if(!$scope.userExists)
                        auth.register($scope.user).success(function(){
                            $scope.user = {};
                            $location.path('/home');
                            mailbox.getUserList().success(function(){
                                keys = Object.keys(userList.data);
                            });
                        });
                });
            };
            $scope.showError = false;
            $scope.loginUser = function(){
                if(!$scope.user.email || !$scope.user.password){ return; }
                auth.login($scope.user)
                    .success(function(){
                    $scope.user = {};
                    $location.path('/home');
                    })
                    .error(function(){
                        $scope.showError = true;
                    });
            };
        }
    ]
);

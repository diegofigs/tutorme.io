/**
 * Created by diegofigs on 10/7/16.
 */
'use strict';

/**
 * @ngdoc function
 * @name publicApp.controller:NavCtrl
 * @description
 * # NavCtrl
 * Controller of the Navbar
 */
angular.module('publicApp')
    .controller('NavCtrl', [
        '$scope',
        'auth',
        function ($scope, auth) {
            $scope.currentUser = auth.currentUser;
            $scope.isLoggedIn = auth.isLoggedIn;
            $scope.logout = auth.logout;
        }
    ]
);
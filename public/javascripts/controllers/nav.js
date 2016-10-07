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
                $scope.isLoggedIn = auth.isLoggedIn();
                $scope.logout = auth.logout();
                $scope.currentUser = auth.currentUser();
            }
        ]
    );
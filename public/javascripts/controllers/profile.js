/**
 * Created by diegofigs on 10/10/16.
 */
'use strict';

/**
 * @ngdoc function
 * @name publicApp.controller:ProfileCtrl
 * @description
 * # ProfileCtrl
 * Controller of the Profile Page
 */
angular.module('publicApp')
    .controller('ProfileCtrl', [
            '$scope',
            '$route',
            'auth',
            function ($scope, $route, auth) {
                $scope.user = auth.currentUser();
                $scope.updatedUser = {
                    firstname: '',
                    lastname: '',
                    email: '',
                    password: ''
                };
                $scope.updateUser = function(){
                    $scope.updatedUser.id = $scope.user.id;
                    $scope.updatedUser.type = $scope.user.type;
                    auth.updateUser($scope.updatedUser).success(function(){
                        $route.reload();
                    });
                };
            }
        ]
    );

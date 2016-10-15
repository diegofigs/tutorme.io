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
            'auth',
            function ($scope, auth) {
                $scope.user = auth.currentUser();
            }
        ]
    );

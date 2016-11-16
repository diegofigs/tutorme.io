/**
 * Created by Andres on 10/8/16.
 */
'use strict';

/**
 * @ngdoc function
 * @name publicApp.controller:MailboxCtrl
 * @description
 * # MailboxCtrl
 * Controller of the Mailbox of TutorMe.io
 */
angular.module('publicApp')
    .controller('MailboxCtrl', [
        '$scope',
        '$route',
        'mailbox',
        function ($scope, $route, mailbox) {

            // var conversations = messagesService.getExistingConversations("user00@example.com");//Current user
            //
            // $scope.activeConversation=conversations[0];
            //
            // $scope.getActiveConversation = function(){
            //     return $scope.activeConversation;
            // };
            //
            // $scope.setActiveConversation = function(email){
            //     $scope.activeConversation = email;
            // };
            //
            // $scope.getConversationBetween = function(email){
            //     return messagesService.getConversationBetween("user00@example.com", email);
            // };

            mailbox.getUserList().then(function(userList){
                var keys = Object.keys(userList.data);
                var values = keys.map(function(v) { return userList.data[v]; });
                $scope.userNamesArray = values;
            });

            // $scope.getUser = function(email){
            //     return dummyUsersService.getDummyUser(email);
            // };
            //
            // $scope.messages = messagesService.getMessageFor("user00@example.com")//Current user
            //
            // $scope.conversations = messagesService.getExistingConversations("user00@example.com")//Current user

        }
    ]
);
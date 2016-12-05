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
        'auth',
        'mailbox',
        function ($scope, $route, auth, mailbox) {

            mailbox.getUserList().then(function(userList){
                var keys = Object.keys(userList.data);
                var values = keys.map(function(v) { return userList.data[v]; });
                $scope.userNamesArray = values;
                $scope.userEmailArray = keys;
                $scope.getUserName = function (email) {
                    return values[keys.indexOf(email)];
                }
            });

            var messagesArray = null;
            var conversations = null;
            mailbox.getMessages().then(function(messages){
                messagesArray = messages.data;
                $scope.messages = messagesArray;
                conversations = getExistingConversations();
                $scope.conversations = conversations;

                $scope.activeConversation=conversations[0];

                $scope.getActiveConversation = function(){
                    return $scope.activeConversation;
                };

                $scope.setActiveConversation = function(email){
                    $scope.activeConversation = email;
                };

                $scope.getConversationBetween = function(email){
                    return getConversationBetween(auth.currentUser().email, email);
                };
            });

            $scope.currentUser = auth.currentUser();

            function getConversationBetween(toEmail, fromEmail){
                var m = []
                for(var i =0;i<messagesArray.length;i++){
                    if(messagesArray[i].to===toEmail && messagesArray[i].from===fromEmail ||
                        messagesArray[i].to===fromEmail && messagesArray[i].from===toEmail){
                        m.push(messagesArray[i]);
                    }
                }
                return m;
            };

            function getExistingConversations(){
                var m = [];
                for(var i =0;i<messagesArray.length;i++){
                    if(messagesArray[i].to===auth.currentUser().email ||
                        messagesArray[i].from!=auth.currentUser().email){
                        m.push(messagesArray[i].from);
                    }
                }
                return uniq(m);//returns emails of distinct conversations
            };

            function uniq(a) {
                var seen = {};
                var out = [];
                var len = a.length;
                var j = 0;
                for(var i = 0; i < len; i++) {
                    var item = a[i];
                    if(seen[item] !== 1) {
                        seen[item] = 1;
                        out[j++] = item;
                    }
                }
                return out;
            }
        }
    ]
);
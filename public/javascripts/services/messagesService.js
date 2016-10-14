/**
 * Created by Andres on 10/14/16.
 */
angular.module('publicApp').service('messagesService', function () {

    this.getMessages = function(){
        return messages;
    };

    var messages =[
        {
            to:'user00@example.com',
            from: 'user01@example.com',
            date: '10/10/16 8:00 pm',
            message: 'When is the DB test?',
        },
        {
            to:'user01@example.com',
            from: 'user00@example.com',
            date: '10/10/16 8:04 pm',
            message: 'No idea',
        },
        {
            to:'user00@example.com',
            from: 'user02@example.com',
            date: '10/10/16 8:05 pm',
            message: 'Hey',
        },
        {
            to:'user00@example.com',
            from: 'user04@example.com',
            date: '10/10/16 8:08 pm',
            message: 'Hello!',
        },
        {
            to:'user00@example.com',
            from: 'user01@example.com',
            date: '10/10/16 8:09 pm',
            message: 'We are getting an F',
        }
    ];

    this.getMessageFor = function(email){
        var m = []
        for(var i =0;i<messages.length;i++){
            if(messages[i].to===email || messages[i].from===email){
                m.push(messages[i]);
            }
        }
        return m;
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

    this.getExistingConversations = function(email){
        var m = [];
        for(var i =0;i<messages.length;i++){
            if(messages[i].to===email){
                m.push(messages[i].from);
            }
        }
        return uniq(m);//returns emails of distinct conversations
    };

    this.getConversationBetween = function(toEmail, fromEmail){
        var m = []
        for(var i =0;i<messages.length;i++){
            if(messages[i].to===toEmail && messages[i].from===fromEmail ||
                messages[i].to===fromEmail && messages[i].from===toEmail){
                m.push(messages[i]);
            }
        }
        return m;
    };

});

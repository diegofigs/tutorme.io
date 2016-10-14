/**
 * Created by Andres on 10/14/16.
 */
angular.module('publicApp').service('dummyUsersService', function () {

    this.getDummyUsers = function(){
        return dummyUsers;
    };

    this.getDummyUser = function(email){
        for(var i =0;i<dummyUsers.length;i++){
            if(dummyUsers[i].email===email){
                return dummyUsers[i];
            }
        }
        return null;
    };

    var dummyUsers =[
        {
            email:'user00@example.com',
            first: 'Abdul',
            last: 'Albino',
            password: '123456',
            icon: 'material-icons circle green'
        },
        {
            email:'user01@example.com',
            first: 'Becki',
            last: 'Banfield',
            password: '123456',
            icon: 'material-icons circle blue'
        },
        {
            email:'user02@example.com',
            first: 'Claribel',
            last: 'Chichester',
            password: '123456',
            icon: 'material-icons circle pink'
        },
        {
            email:'user03@example.com',
            first: 'Delmar',
            last: 'Drain',
            password: '123456',
            icon: 'material-icons circle purple'
        },
        {
            email:'user04@example.com',
            first: 'Exie',
            last: 'Eckley',
            password: '123456',
            icon: 'material-icons circle gray'
        },
        {
            email:'user05@example.com',
            first: 'Francis',
            last: 'Frix',
            password: '123456',
            icon: 'material-icons circle red'
        }
    ];

});

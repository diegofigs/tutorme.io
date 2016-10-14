/**
 * Created by Andres on 10/14/16.
 */
angular.module('publicApp').service('wallPostsService', function () {

    this.getWallPosts = function(){
        return wallPosts;
    };

    var wallPosts =[
        {
            postId: 0,
            courseId: 0,
            from: 'user01@example.com',
            date: '10/10/16 8:00 pm',
            title: 'Chapter 1 doubt',
            message: 'What is a limit?',
            favoriteOf: ['user01@example.com', 'user00@example.com']
        },
        {
            postId: 1,
            courseId: 0,
            from: 'user04@example.com',
            date: '10/10/16 8:10 pm',
            title: 'Help me!',
            message: 'This course is too fun!',
            favoriteOf: []
        },
        {
            postId: 2,
            courseId: 0,
            from: 'user03@example.com',
            date: '10/10/16 8:30 pm',
            title: 'I know nothing',
            message: 'People call me Jon Snow',
            favoriteOf: ['user01@example.com', 'user00@example.com']
        },
        {
            postId: 3,
            courseId: 1,
            from: 'user00@example.com',
            date: '10/10/16 9:00 pm',
            title: 'How did I even pass Calc 1?',
            message: 'What is an integral?',
            favoriteOf: ['user01@example.com']
        },
        {
            postId: 4,
            courseId: 1,
            from: 'user01@example.com',
            date: '10/10/16 9:10 pm',
            title: 'Many doubts',
            message: 'What is life?',
            favoriteOf: ['user00@example.com']
        },
        {
            postId: 5,
            courseId: 2,
            from: 'user03@example.com',
            date: '10/10/16 9:20 pm',
            title: 'Chapter 47 doubt',
            message: 'N dimensions are too many',
            favoriteOf: ['user05@example.com', 'user01@example.com']
        },
        {
            postId: 6,
            courseId: 2,
            from: 'user04@example.com',
            date: '10/10/16 10:30 pm',
            title: 'Triple integrals???',
            message: 'I did not even get single ones',
            favoriteOf: ['user00@example.com', 'user04@example.com']
        },
        {
            postId: 7,
            courseId: 3,
            from: 'user00@example.com',
            date: '10/11/16 12:00 pm',
            title: 'At least something easy',
            message: 'Never mind, just got a test',
            favoriteOf: []
        }
    ];

    this.getPostsInWall = function(c){
        var p = [];
        for(var i =0;i<wallPosts.length;i++){
            if(wallPosts[i].courseId==c) {
                p.push(wallPosts[i]);
            }
        }
        return p;//returns courses of wall of course with ID courseId
    };

    this.isFavoriteOf = function(email, pId){
        for(var i = 0; i<wallPosts[pId].favoriteOf.length; i++){
            if(wallPosts[pId].favoriteOf[i]==email){
                return true;
            }
        }
        return false;
    };

});
/**
 * Created by luisr on 10/13/2016.
 */
angular.module('publicApp').service('lessonsService', function () {

    this.getLessons = function(){
        return lessons;
    };

    var lessons =[
        {
            id:0,
            name: 'Limits',
            videos: [
                {vidTitle:'Video 1', videoURL:''},
                {vidTitle:'Video 2', videoURL:''},
                {vidTitle:'Video 3', videoURL:''}
            ],
            assignments:[
                {asTitle:'Exercises', dueDate: '10/10/16 11:59 pm', attachmentURL:'C:\Users\luisr\OneDrive\Uni\Resume', grade:null, description:'There are 5 exercises. if you have at least 2 wrong answers you will get a 0'}
            ],
            documents: [
                {docTitle:'Presentation', docURL:'C:\Users\luisr\OneDrive\Uni\Resume', docDescription:'This is the presentation used in the videos.'}
            ]
        },
        {
            id:1,
            name: 'Derivatives',
            videos: [
                {vidTitle:'Video 1', videoURL:'https://www.youtube.com/watch?v=Q8TXgCzxEnw'}
            ],
            assignments:[
                {asTitle:'Exercises', dueDate: '10/15/16 11:59 pm', attachmentURL:'C:\Users\luisr\OneDrive\Uni\Resume', grade:null, description:'There are 5 exercises. if you have at least 2 wrong answers you will get a 0'}
            ],
            documents: [
                {docTitle:'Presentation', docURL:'C:\Users\luisr\OneDrive\Uni\Resume', docDescription:'This is the presentation used in the video.'}
            ]
        },
        {
            id:2,
            name: 'Antiderivatives',
            videos: [
                {vidTitle:'Video 1', videoURL:'https://www.youtube.com/watch?v=Q8TXgCzxEnw'}
            ],
            assignments:[
                {asTitle:'Exercises', dueDate: '10/17/16 11:59 pm', attachmentURL:'C:\Users\luisr\OneDrive\Uni\Resume', grade:null, description:'There are 5 exercises. if you have at least 2 wrong answers you will get a 0'}
            ],
            documents: [
                {docTitle:'Presentation', docURL:'C:\Users\luisr\OneDrive\Uni\Resume', docDescription:'This is the presentation used in the video.'}
            ]
        },
        {
            id:3,
            name: 'Definite Integral',
            videos: [
                {vidTitle:'Video 1', videoURL:'https://www.youtube.com/watch?v=Q8TXgCzxEnw'}
            ],
            assignments:[
                {asTitle:'Exercises', dueDate: '10/20/16 11:59 pm', attachmentURL:'C:\Users\luisr\OneDrive\Uni\Resume', grade:null, description:'There are 5 exercises. if you have at least 2 wrong answers you will get a 0'}
            ],
            documents: [
                {docTitle:'Presentation', docURL:'C:\Users\luisr\OneDrive\Uni\Resume', docDescription:'This is the presentation used in the video.'}
            ]
        }
    ];

    this.getVideosOf = function(name){
        return getLesson(name).videos;
    }

    this.getLesson = function(name){
        for(var i =0;i<lessons.length;i++){
            if(lessons[i].name===name){
                return lessons[i];
            }
        }
        return null;
    };



});
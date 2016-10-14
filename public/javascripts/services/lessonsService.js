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
                {vidTitle:'Vid 1', src:'Q8TXgCzxEnw'},
                {vidTitle:'Vid 2', src:'Q8TXgCzxEnw'},
                {vidTitle:'Vid 3', src:'Q8TXgCzxEnw'}
            ],
            assignments:[
                {
                    asId: 0,
                    asTitle: 'Exercises',
                    dueDate: '10/19/16 11:59 pm',
                    attachmentURL: 'C:\Users\luisr\OneDrive\Uni\Resume',
                    description: 'There are 5 exercises. if you have at least 2 wrong answers you will get a 0',
                    submissions: [
                        {
                            sId: 1,
                            sName: 'User 1',
                            grade: null,
                            dateSubmitted: '10/14/16 11:05 am',
                            attachment: 'C:\Users\luisr\OneDrive\Uni\Resume'
                        },
                        {
                            sId: 2,
                            sName: 'User 2',
                            grade: null,
                            dateSubmitted: '10/15/16 1:15 pm',
                            attachment: 'C:\Users\luisr\OneDrive\Uni\Resume'
                        },
                        {
                            sId: 3,
                            sName: 'User 3',
                            grade: null,
                            dateSubmitted: '10/18/16 10:45 pm',
                            attachment: 'C:\Users\luisr\OneDrive\Uni\Resume'
                        }
                    ]
                }
            ],
            documents: [
                {docTitle:'Presentation', docURL:'C:\Users\luisr\OneDrive\Uni\Resume', docDescription:'This is the presentation used in the videos.'}
            ]
        },
        {
            id:1,
            name: 'Derivatives',
            videos: [
                {vidTitle:'V1', src:'Q8TXgCzxEnw'}
            ],
            assignments:[
                {
                    asId:1,
                    asTitle: 'Exercises',
                    dueDate: '10/15/16 11:59 pm',
                    attachmentURL: 'C:\Users\luisr\OneDrive\Uni\Resume',
                    description: 'There are 5 exercises. if you have at least 2 wrong answers you will get a 0',
                    submissions: [
                        {
                            sId: 1,
                            sName: 'User 1',
                            grade: null,
                            dateSubmitted: '10/14/16 11:05 am',
                            attachment: 'C:\Users\luisr\OneDrive\Uni\Resume'
                        },
                        {
                            sId: 2,
                            sName: 'User 2',
                            grade: null,
                            dateSubmitted: '10/15/16 1:15 pm',
                            attachment: 'C:\Users\luisr\OneDrive\Uni\Resume'
                        },
                        {
                            sId: 3,
                            sName: 'User 3',
                            grade: null,
                            dateSubmitted: '10/18/16 10:45 pm',
                            attachment: 'C:\Users\luisr\OneDrive\Uni\Resume'
                        }
                    ]
                }
            ],
            documents: [
                {
                    docTitle: 'Presentation',
                    docURL: 'C:\Users\luisr\OneDrive\Uni\Resume',
                    docDescription: 'This is the presentation used in the video.'
                }
            ]
        },
        {
            id:2,
            name: 'Antiderivatives',
            videos: [
                {
                    vidTitle: 'Video 1', src: 'Q8TXgCzxEnw'
                }
            ],
            assignments:[
                {
                    asId:2,
                    asTitle: 'Exercises',
                    dueDate: '10/17/16 11:59 pm',
                    attachmentURL: 'C:\Users\luisr\OneDrive\Uni\Resume',
                    description: 'There are 5 exercises. if you have at least 2 wrong answers you will get a 0',
                    submissions: [
                        {
                            sId: 1,
                            sName: 'User 1',
                            grade: null,
                            dateSubmitted: '10/14/16 11:05 am',
                            attachment: 'C:\Users\luisr\OneDrive\Uni\Resume'
                        },
                        {
                            sId: 2,
                            sName: 'User 2',
                            grade: null,
                            dateSubmitted: '10/15/16 1:15 pm',
                            attachment: 'C:\Users\luisr\OneDrive\Uni\Resume'
                        },
                        {
                            sId: 3,
                            sName: 'User 3',
                            grade: null,
                            dateSubmitted: '10/18/16 10:45 pm',
                            attachment: 'C:\Users\luisr\OneDrive\Uni\Resume'
                        }
                    ]
                }
            ],
            documents: [
                {
                    docTitle: 'Presentation',
                    docURL: 'C:\Users\luisr\OneDrive\Uni\Resume',
                    docDescription: 'This is the presentation used in the video.'
                }
            ]
        },
        {
            id:3,
            name: 'Definite Integral',
            videos: [
                {vidTitle:'Vide1', src:'Q8TXgCzxEnw'}
            ],
            assignments:[
                {
                    asId:3,
                    asTitle: 'Exercises',
                    dueDate: '10/20/16 11:59 pm',
                    attachmentURL: 'C:\Users\luisr\OneDrive\Uni\Resume',
                    description: 'There are 5 exercises. if you have at least 2 wrong answers you will get a 0',
                    submissions: [
                        {
                            sId: 1,
                            sName: 'User 1',
                            grade: null,
                            dateSubmitted: '10/14/16 11:05 am',
                            attachment: 'C:\Users\luisr\OneDrive\Uni\Resume'
                        },
                        {
                            sId: 2,
                            sName: 'User 2',
                            grade: null,
                            dateSubmitted: '10/15/16 1:15 pm',
                            attachment: 'C:\Users\luisr\OneDrive\Uni\Resume'
                        },
                        {
                            sId: 3,
                            sName: 'User 3',
                            grade: null,
                            dateSubmitted: '10/18/16 10:45 pm',
                            attachment: 'C:\Users\luisr\OneDrive\Uni\Resume'
                        }
                    ]
                }
            ],
            documents: [
                {
                    docTitle: 'Presentation',
                    docURL: 'C:\Users\luisr\OneDrive\Uni\Resume',
                    docDescription: 'This is the presentation used in the video.'
                }
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
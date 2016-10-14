/**
 * Created by Andres on 10/14/16.
 */
angular.module('publicApp').service('dummyCoursesService', function () {

    this.getDummyCourses = function(){
        return dummyCourses;
    };

    var dummyCourses =[
        {
            id: 0,
            title: 'Calculus 1',
            description: 'Math',
        },
        {
            id: 1,
            title: 'Calculus 2',
            description: 'Math',
        },
        {
            id: 2,
            title: 'Calculus 3',
            description: 'Math',
        },
        {
            id: 3,
            title: 'Differential Equations',
            description: 'Math',
        }
    ];

    this.findCourseById = function(courseId){
        return dummyCourses[courseId];
    };

});
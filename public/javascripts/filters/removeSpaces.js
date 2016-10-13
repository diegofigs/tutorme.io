/**
 * Created by luisr on 10/13/2016.
 */
angular.module('publicApp')

    .filter('removeSpaces', [function() {
        return function(string) {
            if (!angular.isString(string)) {
                return string;
            }
            return string.replace(/[\s]/g, '');
        };
    }])
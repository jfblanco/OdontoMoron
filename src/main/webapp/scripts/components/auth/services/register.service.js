'use strict';

angular.module('odontomoronApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });



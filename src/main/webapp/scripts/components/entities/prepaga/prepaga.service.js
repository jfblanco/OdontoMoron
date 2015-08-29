'use strict';

angular.module('odontomoronApp')
    .factory('Prepaga', function ($resource, DateUtils) {
        return $resource('api/prepagas/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });

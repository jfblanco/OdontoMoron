'use strict';

angular.module('odontomoronApp')
    .factory('Odontologo', function ($resource, DateUtils) {
        return $resource('api/odontologos/:id', {}, {
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

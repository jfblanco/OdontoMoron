'use strict';

angular.module('odontomoronApp')
    .factory('Atencion', function ($resource, DateUtils) {
        return $resource('api/atencions/:id', {}, {
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

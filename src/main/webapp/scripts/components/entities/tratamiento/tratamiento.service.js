'use strict';

angular.module('odontomoronApp')
    .factory('Tratamiento', function ($resource, DateUtils) {
        return $resource('api/tratamientos/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.fecha = DateUtils.convertDateTimeFromServer(data.fecha);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });

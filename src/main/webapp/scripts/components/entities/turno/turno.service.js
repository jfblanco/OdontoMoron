'use strict';

angular.module('odontomoronApp')
    .factory('Turno', function ($resource, DateUtils) {
        return $resource('api/turnos/:id', {}, {
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

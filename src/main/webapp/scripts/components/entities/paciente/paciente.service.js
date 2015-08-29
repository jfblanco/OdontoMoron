'use strict';

angular.module('odontomoronApp')
    .factory('Paciente', function ($resource, DateUtils) {
        return $resource('api/pacientes/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.fechaNacimiento = DateUtils.convertDateTimeFromServer(data.fechaNacimiento);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });

'use strict';

angular.module('odontomoronApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('tratamiento', {
                parent: 'entity',
                url: '/tratamiento',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Tratamientos'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/tratamiento/tratamientos.html',
                        controller: 'TratamientoController'
                    }
                },
                resolve: {
                }
            })
            .state('tratamientoDetail', {
                parent: 'entity',
                url: '/tratamiento/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Tratamiento'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/tratamiento/tratamiento-detail.html',
                        controller: 'TratamientoDetailController'
                    }
                },
                resolve: {
                }
            });
    });

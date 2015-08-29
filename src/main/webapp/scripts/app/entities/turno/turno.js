'use strict';

angular.module('odontomoronApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('turno', {
                parent: 'entity',
                url: '/turno',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Turnos'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/turno/turnos.html',
                        controller: 'TurnoController'
                    }
                },
                resolve: {
                }
            })
            .state('turnoDetail', {
                parent: 'entity',
                url: '/turno/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Turno'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/turno/turno-detail.html',
                        controller: 'TurnoDetailController'
                    }
                },
                resolve: {
                }
            });
    });

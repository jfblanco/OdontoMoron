'use strict';

angular.module('odontomoronApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('paciente', {
                parent: 'entity',
                url: '/paciente',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Pacientes'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/paciente/pacientes.html',
                        controller: 'PacienteController'
                    }
                },
                resolve: {
                }
            })
            .state('pacienteDetail', {
                parent: 'entity',
                url: '/paciente/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Paciente'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/paciente/paciente-detail.html',
                        controller: 'PacienteDetailController'
                    }
                },
                resolve: {
                }
            });
    });

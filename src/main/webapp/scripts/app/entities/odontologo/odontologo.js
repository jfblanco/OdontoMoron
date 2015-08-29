'use strict';

angular.module('odontomoronApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('odontologo', {
                parent: 'entity',
                url: '/odontologo',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Odontologos'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/odontologo/odontologos.html',
                        controller: 'OdontologoController'
                    }
                },
                resolve: {
                }
            })
            .state('odontologoDetail', {
                parent: 'entity',
                url: '/odontologo/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Odontologo'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/odontologo/odontologo-detail.html',
                        controller: 'OdontologoDetailController'
                    }
                },
                resolve: {
                }
            });
    });

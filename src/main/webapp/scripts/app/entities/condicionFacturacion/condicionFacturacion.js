'use strict';

angular.module('odontomoronApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('condicionFacturacion', {
                parent: 'entity',
                url: '/condicionFacturacion',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'CondicionFacturacions'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/condicionFacturacion/condicionFacturacions.html',
                        controller: 'CondicionFacturacionController'
                    }
                },
                resolve: {
                }
            })
            .state('condicionFacturacionDetail', {
                parent: 'entity',
                url: '/condicionFacturacion/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'CondicionFacturacion'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/condicionFacturacion/condicionFacturacion-detail.html',
                        controller: 'CondicionFacturacionDetailController'
                    }
                },
                resolve: {
                }
            });
    });

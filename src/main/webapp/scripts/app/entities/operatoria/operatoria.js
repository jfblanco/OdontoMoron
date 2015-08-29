'use strict';

angular.module('odontomoronApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('operatoria', {
                parent: 'entity',
                url: '/operatoria',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Operatorias'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/operatoria/operatorias.html',
                        controller: 'OperatoriaController'
                    }
                },
                resolve: {
                }
            })
            .state('operatoriaDetail', {
                parent: 'entity',
                url: '/operatoria/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Operatoria'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/operatoria/operatoria-detail.html',
                        controller: 'OperatoriaDetailController'
                    }
                },
                resolve: {
                }
            });
    });

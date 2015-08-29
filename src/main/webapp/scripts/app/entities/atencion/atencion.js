'use strict';

angular.module('odontomoronApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('atencion', {
                parent: 'entity',
                url: '/atencion',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Atencions'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/atencion/atencions.html',
                        controller: 'AtencionController'
                    }
                },
                resolve: {
                }
            })
            .state('atencionDetail', {
                parent: 'entity',
                url: '/atencion/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Atencion'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/atencion/atencion-detail.html',
                        controller: 'AtencionDetailController'
                    }
                },
                resolve: {
                }
            });
    });

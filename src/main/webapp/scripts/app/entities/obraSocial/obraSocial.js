'use strict';

angular.module('odontomoronApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('obraSocial', {
                parent: 'entity',
                url: '/obraSocial',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'ObraSocials'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/obraSocial/obraSocials.html',
                        controller: 'ObraSocialController'
                    }
                },
                resolve: {
                }
            })
            .state('obraSocialDetail', {
                parent: 'entity',
                url: '/obraSocial/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'ObraSocial'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/obraSocial/obraSocial-detail.html',
                        controller: 'ObraSocialDetailController'
                    }
                },
                resolve: {
                }
            });
    });

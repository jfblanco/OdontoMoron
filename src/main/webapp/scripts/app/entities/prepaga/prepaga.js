'use strict';

angular.module('odontomoronApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('prepaga', {
                parent: 'entity',
                url: '/prepaga',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Prepagas'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/prepaga/prepagas.html',
                        controller: 'PrepagaController'
                    }
                },
                resolve: {
                }
            })
            .state('prepagaDetail', {
                parent: 'entity',
                url: '/prepaga/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Prepaga'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/prepaga/prepaga-detail.html',
                        controller: 'PrepagaDetailController'
                    }
                },
                resolve: {
                }
            });
    });

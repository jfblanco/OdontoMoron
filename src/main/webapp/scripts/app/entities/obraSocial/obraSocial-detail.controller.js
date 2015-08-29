'use strict';

angular.module('odontomoronApp')
    .controller('ObraSocialDetailController', function ($scope, $stateParams, ObraSocial) {
        $scope.obraSocial = {};
        $scope.load = function (id) {
            ObraSocial.get({id: id}, function(result) {
              $scope.obraSocial = result;
            });
        };
        $scope.load($stateParams.id);
    });

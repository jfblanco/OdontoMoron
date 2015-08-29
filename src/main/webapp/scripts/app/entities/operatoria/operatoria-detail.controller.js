'use strict';

angular.module('odontomoronApp')
    .controller('OperatoriaDetailController', function ($scope, $stateParams, Operatoria, ObraSocial) {
        $scope.operatoria = {};
        $scope.load = function (id) {
            Operatoria.get({id: id}, function(result) {
              $scope.operatoria = result;
            });
        };
        $scope.load($stateParams.id);
    });

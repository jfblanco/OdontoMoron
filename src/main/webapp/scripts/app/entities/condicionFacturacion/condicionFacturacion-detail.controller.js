'use strict';

angular.module('odontomoronApp')
    .controller('CondicionFacturacionDetailController', function ($scope, $stateParams, CondicionFacturacion) {
        $scope.condicionFacturacion = {};
        $scope.load = function (id) {
            CondicionFacturacion.get({id: id}, function(result) {
              $scope.condicionFacturacion = result;
            });
        };
        $scope.load($stateParams.id);
    });

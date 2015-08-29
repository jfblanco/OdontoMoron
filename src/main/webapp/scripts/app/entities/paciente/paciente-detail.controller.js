'use strict';

angular.module('odontomoronApp')
    .controller('PacienteDetailController', function ($scope, $stateParams, Paciente, Prepaga, Turno) {
        $scope.paciente = {};
        $scope.load = function (id) {
            Paciente.get({id: id}, function(result) {
              $scope.paciente = result;
            });
        };
        $scope.load($stateParams.id);
    });

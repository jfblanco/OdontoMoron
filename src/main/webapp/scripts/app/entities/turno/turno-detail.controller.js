'use strict';

angular.module('odontomoronApp')
    .controller('TurnoDetailController', function ($scope, $stateParams, Turno, Tratamiento, Atencion) {
        $scope.turno = {};
        $scope.load = function (id) {
            Turno.get({id: id}, function(result) {
              $scope.turno = result;
            });
        };
        $scope.load($stateParams.id);
    });

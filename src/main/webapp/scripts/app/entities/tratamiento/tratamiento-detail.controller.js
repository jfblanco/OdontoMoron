'use strict';

angular.module('odontomoronApp')
    .controller('TratamientoDetailController', function ($scope, $stateParams, Tratamiento, Operatoria) {
        $scope.tratamiento = {};
        $scope.load = function (id) {
            Tratamiento.get({id: id}, function(result) {
              $scope.tratamiento = result;
            });
        };
        $scope.load($stateParams.id);
    });

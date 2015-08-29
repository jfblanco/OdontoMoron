'use strict';

angular.module('odontomoronApp')
    .controller('OdontologoDetailController', function ($scope, $stateParams, Odontologo) {
        $scope.odontologo = {};
        $scope.load = function (id) {
            Odontologo.get({id: id}, function(result) {
              $scope.odontologo = result;
            });
        };
        $scope.load($stateParams.id);
    });

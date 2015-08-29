'use strict';

angular.module('odontomoronApp')
    .controller('AtencionDetailController', function ($scope, $stateParams, Atencion, Odontologo) {
        $scope.atencion = {};
        $scope.load = function (id) {
            Atencion.get({id: id}, function(result) {
              $scope.atencion = result;
            });
        };
        $scope.load($stateParams.id);
    });

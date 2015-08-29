'use strict';

angular.module('odontomoronApp')
    .controller('PrepagaDetailController', function ($scope, $stateParams, Prepaga, CondicionFacturacion) {
        $scope.prepaga = {};
        $scope.load = function (id) {
            Prepaga.get({id: id}, function(result) {
              $scope.prepaga = result;
            });
        };
        $scope.load($stateParams.id);
    });

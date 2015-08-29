'use strict';

angular.module('odontomoronApp')
    .controller('OdontologoController', function ($scope, Odontologo) {
        $scope.odontologos = [];
        $scope.loadAll = function() {
            Odontologo.query(function(result) {
               $scope.odontologos = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Odontologo.get({id: id}, function(result) {
                $scope.odontologo = result;
                $('#saveOdontologoModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.odontologo.id != null) {
                Odontologo.update($scope.odontologo,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Odontologo.save($scope.odontologo,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Odontologo.get({id: id}, function(result) {
                $scope.odontologo = result;
                $('#deleteOdontologoConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Odontologo.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteOdontologoConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveOdontologoModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.odontologo = {nombre: null, apellido: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });

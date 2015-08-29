'use strict';

angular.module('odontomoronApp')
    .controller('TratamientoController', function ($scope, Tratamiento, Operatoria) {
        $scope.tratamientos = [];
        $scope.operatorias = Operatoria.query();
        $scope.loadAll = function() {
            Tratamiento.query(function(result) {
               $scope.tratamientos = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Tratamiento.get({id: id}, function(result) {
                $scope.tratamiento = result;
                $('#saveTratamientoModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.tratamiento.id != null) {
                Tratamiento.update($scope.tratamiento,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Tratamiento.save($scope.tratamiento,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Tratamiento.get({id: id}, function(result) {
                $scope.tratamiento = result;
                $('#deleteTratamientoConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Tratamiento.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTratamientoConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveTratamientoModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.tratamiento = {fecha: null, conformidad: null, cara: null, sector: null, pieza: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });

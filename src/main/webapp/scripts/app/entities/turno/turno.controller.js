'use strict';

angular.module('odontomoronApp')
    .controller('TurnoController', function ($scope, Turno, Tratamiento, Atencion) {
        $scope.turnos = [];
        $scope.tratamientos = Tratamiento.query();
        $scope.atencions = Atencion.query();
        $scope.loadAll = function() {
            Turno.query(function(result) {
               $scope.turnos = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Turno.get({id: id}, function(result) {
                $scope.turno = result;
                $('#saveTurnoModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.turno.id != null) {
                Turno.update($scope.turno,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Turno.save($scope.turno,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Turno.get({id: id}, function(result) {
                $scope.turno = result;
                $('#deleteTurnoConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Turno.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTurnoConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveTurnoModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.turno = {fecha: null, observacion: null, sobreturno: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });

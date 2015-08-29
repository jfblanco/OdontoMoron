'use strict';

angular.module('odontomoronApp')
    .controller('PacienteController', function ($scope, Paciente, Prepaga, Turno) {
        $scope.pacientes = [];
        $scope.prepagas = Prepaga.query();
        $scope.turnos = Turno.query();
        $scope.loadAll = function() {
            Paciente.query(function(result) {
               $scope.pacientes = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Paciente.get({id: id}, function(result) {
                $scope.paciente = result;
                $('#savePacienteModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.paciente.id != null) {
                Paciente.update($scope.paciente,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Paciente.save($scope.paciente,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Paciente.get({id: id}, function(result) {
                $scope.paciente = result;
                $('#deletePacienteConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Paciente.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePacienteConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#savePacienteModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.paciente = {numeroAsociado: null, apellido: null, nombre: null, fechaNacimiento: null, sexo: null, domicilio: null, telefono: null, celular: null, dni: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });

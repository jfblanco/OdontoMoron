'use strict';

angular.module('odontomoronApp')
    .controller('OperatoriaController', function ($scope, Operatoria, ObraSocial) {
        $scope.operatorias = [];
        $scope.obrasocials = ObraSocial.query();
        $scope.loadAll = function() {
            Operatoria.query(function(result) {
               $scope.operatorias = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Operatoria.get({id: id}, function(result) {
                $scope.operatoria = result;
                $('#saveOperatoriaModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.operatoria.id != null) {
                Operatoria.update($scope.operatoria,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Operatoria.save($scope.operatoria,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Operatoria.get({id: id}, function(result) {
                $scope.operatoria = result;
                $('#deleteOperatoriaConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Operatoria.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteOperatoriaConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveOperatoriaModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.operatoria = {descripcion: null, codigo: null, precio: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });

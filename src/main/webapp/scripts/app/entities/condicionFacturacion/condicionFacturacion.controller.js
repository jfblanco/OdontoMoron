'use strict';

angular.module('odontomoronApp')
    .controller('CondicionFacturacionController', function ($scope, CondicionFacturacion) {
        $scope.condicionFacturacions = [];
        $scope.loadAll = function() {
            CondicionFacturacion.query(function(result) {
               $scope.condicionFacturacions = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            CondicionFacturacion.get({id: id}, function(result) {
                $scope.condicionFacturacion = result;
                $('#saveCondicionFacturacionModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.condicionFacturacion.id != null) {
                CondicionFacturacion.update($scope.condicionFacturacion,
                    function () {
                        $scope.refresh();
                    });
            } else {
                CondicionFacturacion.save($scope.condicionFacturacion,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            CondicionFacturacion.get({id: id}, function(result) {
                $scope.condicionFacturacion = result;
                $('#deleteCondicionFacturacionConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            CondicionFacturacion.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteCondicionFacturacionConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveCondicionFacturacionModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.condicionFacturacion = {descripcion: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });

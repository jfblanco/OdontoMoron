'use strict';

angular.module('odontomoronApp')
    .controller('PrepagaController', function ($scope, Prepaga, CondicionFacturacion) {
        $scope.prepagas = [];
        $scope.condicionfacturacions = CondicionFacturacion.query();
        $scope.loadAll = function() {
            Prepaga.query(function(result) {
               $scope.prepagas = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Prepaga.get({id: id}, function(result) {
                $scope.prepaga = result;
                $('#savePrepagaModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.prepaga.id != null) {
                Prepaga.update($scope.prepaga,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Prepaga.save($scope.prepaga,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Prepaga.get({id: id}, function(result) {
                $scope.prepaga = result;
                $('#deletePrepagaConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Prepaga.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePrepagaConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#savePrepagaModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.prepaga = {numeroAfiliado: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });

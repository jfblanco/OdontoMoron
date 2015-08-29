'use strict';

angular.module('odontomoronApp')
    .controller('AtencionController', function ($scope, Atencion, Odontologo) {
        $scope.atencions = [];
        $scope.odontologos = Odontologo.query();
        $scope.loadAll = function() {
            Atencion.query(function(result) {
               $scope.atencions = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Atencion.get({id: id}, function(result) {
                $scope.atencion = result;
                $('#saveAtencionModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.atencion.id != null) {
                Atencion.update($scope.atencion,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Atencion.save($scope.atencion,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Atencion.get({id: id}, function(result) {
                $scope.atencion = result;
                $('#deleteAtencionConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Atencion.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteAtencionConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveAtencionModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.atencion = {ingreso: null, egreso: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });

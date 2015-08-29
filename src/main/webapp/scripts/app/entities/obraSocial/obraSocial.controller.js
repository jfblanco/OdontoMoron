'use strict';

angular.module('odontomoronApp')
    .controller('ObraSocialController', function ($scope, ObraSocial) {
        $scope.obraSocials = [];
        $scope.loadAll = function() {
            ObraSocial.query(function(result) {
               $scope.obraSocials = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            ObraSocial.get({id: id}, function(result) {
                $scope.obraSocial = result;
                $('#saveObraSocialModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.obraSocial.id != null) {
                ObraSocial.update($scope.obraSocial,
                    function () {
                        $scope.refresh();
                    });
            } else {
                ObraSocial.save($scope.obraSocial,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            ObraSocial.get({id: id}, function(result) {
                $scope.obraSocial = result;
                $('#deleteObraSocialConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            ObraSocial.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteObraSocialConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveObraSocialModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.obraSocial = {nombre: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });

'use strict';

angular.module('aulaAluraApp')
	.controller('AlunoDeleteController', function($scope, $uibModalInstance, entity, Aluno) {

        $scope.aluno = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Aluno.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });

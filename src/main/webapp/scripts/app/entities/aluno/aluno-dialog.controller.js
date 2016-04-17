'use strict';

angular.module('aulaAluraApp').controller('AlunoDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Aluno',
        function($scope, $stateParams, $uibModalInstance, entity, Aluno) {

        $scope.aluno = entity;
        $scope.load = function(id) {
            Aluno.get({id : id}, function(result) {
                $scope.aluno = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('aulaAluraApp:alunoUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.aluno.id != null) {
                Aluno.update($scope.aluno, onSaveSuccess, onSaveError);
            } else {
                Aluno.save($scope.aluno, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForDataNascimento = {};

        $scope.datePickerForDataNascimento.status = {
            opened: false
        };

        $scope.datePickerForDataNascimentoOpen = function($event) {
            $scope.datePickerForDataNascimento.status.opened = true;
        };
}]);

'use strict';

angular.module('aulaAluraApp')
    .controller('AlunoDetailController', function ($scope, $rootScope, $stateParams, entity, Aluno) {
        $scope.aluno = entity;
        $scope.load = function (id) {
            Aluno.get({id: id}, function(result) {
                $scope.aluno = result;
            });
        };
        var unsubscribe = $rootScope.$on('aulaAluraApp:alunoUpdate', function(event, result) {
            $scope.aluno = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

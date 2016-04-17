'use strict';

angular.module('aulaAluraApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('aluno', {
                parent: 'entity',
                url: '/alunos',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'aulaAluraApp.aluno.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/aluno/alunos.html',
                        controller: 'AlunoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('aluno');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('aluno.detail', {
                parent: 'entity',
                url: '/aluno/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'aulaAluraApp.aluno.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/aluno/aluno-detail.html',
                        controller: 'AlunoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('aluno');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Aluno', function($stateParams, Aluno) {
                        return Aluno.get({id : $stateParams.id});
                    }]
                }
            })
            .state('aluno.new', {
                parent: 'aluno',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/aluno/aluno-dialog.html',
                        controller: 'AlunoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    nome: null,
                                    idade: null,
                                    endereco: null,
                                    dataNascimento: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('aluno', null, { reload: true });
                    }, function() {
                        $state.go('aluno');
                    })
                }]
            })
            .state('aluno.edit', {
                parent: 'aluno',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/aluno/aluno-dialog.html',
                        controller: 'AlunoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Aluno', function(Aluno) {
                                return Aluno.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('aluno', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('aluno.delete', {
                parent: 'aluno',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/aluno/aluno-delete-dialog.html',
                        controller: 'AlunoDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Aluno', function(Aluno) {
                                return Aluno.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('aluno', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

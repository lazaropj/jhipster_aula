'use strict';

angular.module('aulaAluraApp')
    .factory('Aluno', function ($resource, DateUtils) {
        return $resource('api/alunos/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dataNascimento = DateUtils.convertLocaleDateFromServer(data.dataNascimento);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dataNascimento = DateUtils.convertLocaleDateToServer(data.dataNascimento);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dataNascimento = DateUtils.convertLocaleDateToServer(data.dataNascimento);
                    return angular.toJson(data);
                }
            }
        });
    });

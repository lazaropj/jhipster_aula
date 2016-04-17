'use strict';

angular.module('aulaAluraApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });



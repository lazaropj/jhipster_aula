 'use strict';

angular.module('aulaAluraApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-aulaAluraApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-aulaAluraApp-params')});
                }
                return response;
            }
        };
    });

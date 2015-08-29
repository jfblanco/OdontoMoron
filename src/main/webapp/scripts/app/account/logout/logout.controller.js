'use strict';

angular.module('odontomoronApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });

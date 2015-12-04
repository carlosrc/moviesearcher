'use strict';

var app = angular.module( 'myApp', ['ngResource', 'ngMaterial'] );

app.controller('MainController', ['$scope', 'Servicios', function($scope, Servicios) {
	$scope.query = "";
	$scope.movies = [];

	console.log("Controller!" + Servicios);

	$scope.search = function () {
		$scope.movies = [];
        Servicios.search($scope.query, $scope.movies);
    };

    $scope.searchAll = function () {
    	$scope.movies = [];
    	Servicios.search("*:*", $scope.movies);
    }

    $scope.index = function () {
		$scope.movies = [];
        Servicios.index($scope.movies);
    };

    // Inicializamos la búsqueda mostrando todas las películas
    $scope.searchAll();


    $scope.showCustomToast = function() {
      $mdToast.show({
        controller: 'ToastCtrl',
        templateUrl: 'toast-template.html',
        parent : $document[0].querySelector('#toastBounds'),
        hideDelay: 6000,
        position: $scope.getToastPosition()
      });
    };

}]);

app.config(function($mdThemingProvider) {
	// Temas
	$mdThemingProvider.theme('primary-theme', 'default').primaryPalette('teal').accentPalette('green').warnPalette('red');
});




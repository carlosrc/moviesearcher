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

    $scope.index = function () {
		$scope.movies = [];
        Servicios.index($scope.movies);
    };


}]);

app.config(function($mdThemingProvider) {
	// Temas
	$mdThemingProvider.theme('primary-theme', 'default').primaryPalette('teal').accentPalette('green').warnPalette('red');
});

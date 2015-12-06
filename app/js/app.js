'use strict';

var app = angular.module( 'myApp', ['ngResource', 'ngMaterial', 'ngAnimate'] );

app.controller('MainController', ['$scope', 'Servicios', function($scope, Servicios, $mdToast, $document) {
	console.log("Controller!");

	$scope.resetSearch = function () {
		$scope.query = "";
		$scope.title = null;
		$scope.description = null;
		$scope.runtime = null;
		$scope.yearInit = 0;
		$scope.yearEnd = 0;
		$scope.minVote = 0;
	};
	$scope.movies = [];
	$scope.resetSearch();
	$scope.loading = {valor: false};

	$scope.search = function () {
		$scope.movies = [];
		$scope.loading.valor = true;
        Servicios.search($scope.query, $scope.title, $scope.description, $scope.runtime,
         $scope.yearInit, $scope.yearEnd, $scope.minVote, $scope.movies, $scope.loading);
    };

    $scope.searchAll = function () {
    	$scope.movies = [];
    	$scope.loading.valor = true;
    	var result = Servicios.searchAll("*:*", $scope.movies, $scope.loading);
    }

    $scope.index = function () {
		$scope.movies = [];
		$scope.loading.valor = true;
        Servicios.index($scope.movies, $scope.loading);
    };

    // Inicializamos la búsqueda mostrando todas las películas
    $scope.searchAll();


}]);


app.config(function($mdThemingProvider) {
	// Temas
	$mdThemingProvider.theme('primary-theme', 'default').primaryPalette('teal').accentPalette('pink').warnPalette('red');
});




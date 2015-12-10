'use strict';

var app = angular.module( 'myApp', ['ngResource', 'ngMaterial', 'ngAnimate', 'ui.bootstrap'] );


app.controller('MainController', ['$scope', 'Servicios', '$mdConstant', '$mdDialog', '$mdMedia', '$document',  
	function($scope, Servicios, $mdConstant, $mdDialog, $mdMedia, $document) {

	// Separador en el elemento Chips
	$scope.keys = [$mdConstant.KEY_CODE.ENTER, $mdConstant.KEY_CODE.COMMA];

	$scope.resetSearch = function () {
		$scope.query = "";
		$scope.title = null;
		$scope.description = null;
		$scope.runtime = null;
		$scope.yearInit = 0;
		$scope.yearEnd = 0;
		$scope.minVote = 0;
		$scope.genres = [];
		$scope.cast = [];
		$scope.director = [];
		$scope.strict = false;
	};
	$scope.movies = [];
	$scope.similarMovies = [];
	$scope.resetSearch();
	$scope.loading = {valor: false};
	$scope.loadingSimilar = {valor: false};

	$scope.clean = function () {
		$scope.resetSearch();
    	$scope.movies = [];
    	$scope.loading.valor = true;
	}

	$scope.search = function () {
		$scope.movies = [];
		$scope.loading.valor = true;
        Servicios.search($scope.query, $scope.title, $scope.description, $scope.runtime,
         $scope.yearInit, $scope.yearEnd, $scope.minVote, $scope.genres, $scope.cast, $scope.director, 
         $scope.strict, $scope.movies, $scope.loading);
    };

    $scope.searchAll = function () {
    	$scope.clean();
    	Servicios.searchAll("*:*", $scope.movies, $scope.loading);
    }

    $scope.index = function () {
    	$scope.clean();
        Servicios.index($scope.movies, $scope.loading);
    };

    $scope.searchByGenre = function (genre) {
    	$scope.clean();
    	$scope.genres.push(genre);
		Servicios.search(null, null, null, null, null, null, null, $scope.genres, null, null,
         false, $scope.movies, $scope.loading);
    };

    $scope.searchByDirector = function (director) {
    	$scope.clean();
    	$scope.director.push(director);
		Servicios.search(null, null, null, null, null, null, null, null, null, $scope.director,
         false, $scope.movies, $scope.loading);
    };

    $scope.searchByCast = function (actor) {
    	$scope.clean();
    	$scope.cast.push(actor);
		Servicios.search(null, null, null, null, null, null, null, null, $scope.cast, null,
         false, $scope.movies, $scope.loading);
    };

    $scope.findSimilar = function (id) {
    	$scope.similarMovies = [];
    	$scope.loadingSimilar.valor = true;
		Servicios.findSimilar(id, $scope.similarMovies, $scope.loadingSimilar);
    };


    // Inicializamos la búsqueda mostrando todas las películas
    $scope.searchAll();


	// Diálogo de Películas Similares
	$scope.showSimilarMovies = function(ev, id) {

		$scope.findSimilar(id);

	    $mdDialog.show({
			controller: DialogController,
			templateUrl: 'dialog_similarMovies.html',
			parent: angular.element(document.body),
			targetEvent: ev,
			clickOutsideToClose: true,
			fullscreen: $mdMedia('sm') && $scope.customFullscreen,
			locals: {
				similarMovies: $scope.similarMovies,
				loadingSimilar: $scope.loadingSimilar
			}
	    })
	    .then(function(answer) {
	    	$scope.status = 'You said the information was "' + answer + '".';
	    }, function() {
	    	$scope.status = 'You cancelled the dialog.';
	    });
	    $scope.$watch(function() {
	    	return $mdMedia('sm');
	    }, function(sm) {
	    	$scope.customFullscreen = (sm === true);
	    });
	  };

	$scope.isCollapsed = true;



}]);


function DialogController($scope, $mdDialog, locals) {

	$scope.locals = locals;

	$scope.hide = function() {
		$mdDialog.hide();
	};
	$scope.cancel = function() {
		$mdDialog.cancel();
	};
	$scope.answer = function(answer) {
		$mdDialog.hide(answer);
	};
};

app.config(function($mdThemingProvider) {
	// Temas
	$mdThemingProvider.theme('primary-theme', 'default').primaryPalette('teal').accentPalette('pink').warnPalette('red');
});


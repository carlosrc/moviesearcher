// (function(){     
'use strict';

var app = angular.module( 'myApp', ['ngResource', 'ngMaterial'] );

app.service('Servicios', ['$resource', function($resource) {

	// $httpProvider.defaults.useXDomain = true;
    // delete $httpProvider.defaults.headers.common['X-Requested-With'];

	this.index = function (movies) {
   		console.log("Index method");
		var MovieService = $resource('http://www.localhost:8080/moviesearcher/indexar', 
			{}, {query: {method: 'GET',  isArray: true}});
		
		// TODO: Mostrar toast cuando obtenga true o false. Bloquear pantalla?
		MovieService.query({}, function(movie) {
			console.log(movie.Resource);
		    angular.forEach(movie, function (item) {
		    	console.log(item);
		        if (item.title) {
		            movies.push(item);
		        }	        
    		});
		});
	};

    this.search = function (query, movies) {
   		console.log("Search method:" + query);
     	// return $resource('http://localhost:8080/moviesearcher/search?q=:q',
      //   	{q: 'spectre'},
      //   	{getIssue: {method: 'GET', params: {q: "spectre"}}});



		var MovieService = $resource('http://www.localhost:8080/moviesearcher/search', 
			{}, {query: {method: 'GET',  isArray: true, params: {q: query}}});
			//{ q:'@q' }, // Query parameters
	      	//{'query': { method: 'GET' }});
		
		MovieService.query({q:query}, function(movie) {
			console.log(movie.Resource);
		    angular.forEach(movie, function (item) {
		    	console.log(item);
		        if (item.title) {
		            movies.push(item);
		        }	        
    		});
		});

    };
}]);

app.controller('MainController', ['$scope', 'Servicios', function($scope, Servicios) {
	$scope.titulo = "";
	$scope.movies = [];

	console.log("Controller!" + Servicios);

	$scope.search = function () {
		$scope.movies = [];
        Servicios.search($scope.titulo, $scope.movies);
    };

    $scope.index = function () {
		$scope.movies = [];
        Servicios.index($scope.movies);
    };


}]);

app.config(function($mdThemingProvider) {
	$mdThemingProvider.theme('primary-theme', 'default').primaryPalette('teal').accentPalette('red').warnPalette('red');
});


//});
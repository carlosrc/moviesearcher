'use strict';

app.service('Servicios', ['$resource', function($resource) {

  // $httpProvider.defaults.useXDomain = true;
  // delete $httpProvider.defaults.headers.common['X-Requested-With'];

  this.index = function (movies) {
      console.log("Index method");
    var MovieService = $resource('http://www.localhost:8080/moviesearcher/indexar', 
      {}, {query: {method: 'GET',  isArray: true}});
    
    // TODO: Mostrar toast cuando obtenga true o false. Bloquear pantalla?
    MovieService.query({}, function(movie) {
        angular.forEach(movie, function (item) {
            if (item.title) {
                movies.push(item);
            }         
        });
    });
  };

    this.search = function (query, movies) {
      console.log("Search method:" + query);
    var MovieService = $resource('http://www.localhost:8080/moviesearcher/search', 
      {}, {query: {method: 'GET',  isArray: true, params: {q: query}}});

    MovieService.query({q:query}, function(movie) {
        angular.forEach(movie, function (item) {
            if (item.title) {
                movies.push(item);
            }         
        });
    });

    };
}]);
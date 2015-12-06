'use strict';

app.service('Servicios', ['$resource', '$mdToast', function($resource) {

  this.index = function (movies, loading) {
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
          loading.valor = false;
      }, function (error) {
          loading.valor = false;
      });
  };

  this.searchAll = function (query, movies, loading) {
    this.search(null, null, null, null, 0, 0, 0.01, movies, loading);
  }

  this.search = function (query, title, description, runtime, yearInit, yearEnd, minVote, movies, loading) {
    console.log("Search method:" + query);
    var MovieService = $resource('http://www.localhost:8080/moviesearcher/search', 
      {}, {query: {method: 'GET',  isArray: true, params: {q: query, tit: title, desc: description, runtime: runtime, 
        yearInit: yearInit, yearEnd: yearEnd, minVote: minVote}}});

    MovieService.query({q:query}, function(movie) {
        angular.forEach(movie, function (item) {
            if (item.title) {
                movies.push(item);
            }
        });

        loading.valor = false;
    }, function (error) {
        loading.valor = false;
    });
  };

}]);
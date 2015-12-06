'use strict';

app.service('Servicios', ['$resource', '$mdToast', function($resource, $mdToast) {

  this.servidor = 'http://www.localhost:8080';
  // Tomcat Raspberry
  // this.servidor = 'http://www.192.168.0.192:8082';

  this.index = function (movies, loading) {
      console.log("Index method");
      var MovieService = $resource(this.servidor + '/moviesearcher/index', 
        {}, {query: {method: 'GET',  isArray: true}});
      
      MovieService.query({}, function(movie) {       
          angular.forEach(movie, function (item) {
              if (item.title) {
                  movies.push(item);
              }         
          });
          loading.valor = false;
      }, function (error) {
          loading.valor = false;
          scope.showSimpleToast('Ha ocurrido un error');
      });
  };

  this.searchAll = function (query, movies, loading) {
    this.search(null, null, null, null, 0, 0, 0.01, null, null, null, movies, loading);
  }

  this.search = function (query, title, description, runtime, yearInit, yearEnd, minVote, genres, cast, director, movies, loading) {
    console.log("Search method:" + query);
    var MovieService = $resource(this.servidor + '/moviesearcher/search', 
      {}, {query: {method: 'GET',  isArray: true, params: {q: query, tit: title, desc: description, runtime: runtime, 
        yearInit: yearInit, yearEnd: yearEnd, minVote: minVote, genres: genres, cast: cast, director: director}}});

    MovieService.query({q:query}, function(movie) {
        angular.forEach(movie, function (item) {
            if (item.title) {
                movies.push(item);
            }
        });
        loading.valor = false;
    }, function (error) {
        loading.valor = false;
        scope.showSimpleToast('Ha ocurrido un error');
    });
  };

  this.findSimilar = function (id, similarMovies, loadingSimilar) {
      console.log("Find similar method: " + id);
      var MovieService = $resource(this.servidor + '/moviesearcher/findSimilar', 
        {}, {query: {method: 'GET',  isArray: true,  params: {id: id}}});
      
      MovieService.query({id:id}, function(movies) {       
          angular.forEach(movies, function (item) {
              if (item.title) {
                  similarMovies.push(item);
              }              
          });
          console.log(similarMovies);
          loadingSimilar.valor = false;
      }, function (error) {
          loadingSimilar.valor = false;
          scope.showSimpleToast('Ha ocurrido un error');
      });
  };



  // Funciones del Toast
  var scope = this;

  var last = {
    bottom: true,
    top: false,
    left: false,
    right: true
  };
  this.toastPosition = angular.extend({},last);
  this.getToastPosition = function() {
    sanitizePosition();
    return Object.keys(this.toastPosition)
      .filter(function(pos) { return scope.toastPosition[pos]; })
      .join(' ');
  };
  function sanitizePosition() {
    var current = scope.toastPosition;
    if ( current.bottom && last.top ) current.top = false;
    if ( current.top && last.bottom ) current.bottom = false;
    if ( current.right && last.left ) current.left = false;
    if ( current.left && last.right ) current.right = false;
    last = angular.extend({},current);
  }
  this.showSimpleToast = function(mensaje) {
    $mdToast.show(
      $mdToast.simple()
        .textContent(mensaje)
        .position(this.getToastPosition())
        .hideDelay(3000)
    );
  };

}]);
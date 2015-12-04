'use strict';

app.service('Servicios', ['ngResource'])
  .factory('AngularIssues', function($resource){

    this.prueba = function () {
      console.log("Hola service!");
      return $resource('http://localhost:8080/moviesearcher/search?q=:q',
        {q: '@q'},
        {getIssue: {method: 'GET', params: {q: "spectre"}}});
    };
})
.value('version', '0.1');

// services.config(['$resourceProvider', function($resourceProvider) {
//   // Don't strip trailing slashes from calculated URLs
//   $resourceProvider.defaults.stripTrailingSlashes = false;
// }]);



/*
{ 'get':    {method:'GET'},
  'save':   {method:'POST'},
  'query':  {method:'GET', isArray:true},
  'remove': {method:'DELETE'},
  'delete': {method:'DELETE'} };
*/
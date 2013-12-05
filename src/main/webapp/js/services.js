var organixServices = angular.module('organixServices', ['ngResource']);
     
organixServices.factory('ObjectType', ['$resource', function($resource){

	return $resource('/organix/objectType/');
}]);
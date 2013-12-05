var organixApp = angular.module('organixApp', ['ngRoute', 'organixFilters', 'organixServices', 'organixControllers']);

organixApp.config(['$routeProvider', function($routeProvider) {

	$routeProvider.
	when('/configuration', { templateUrl: 'partials/config-list.html', controller: 'ConfigurationCtrl'}).
	when('/configuration/objectTypes/:typeId', { templateUrl: 'partials/objectType-detail.html', controller: 'ObjectTypeDetailCtrl'}).
	when('/configuration/connectionTypes/:typeId', { templateUrl: 'partials/connectionType-detail.html', controller: 'ConnectionTypeDetailCtrl'}).
	otherwise({redirectTo: '/configuration'});

}]);
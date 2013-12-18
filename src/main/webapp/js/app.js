var organixApp = angular.module('organixApp', ['ngRoute', 'organixFilters', 'organixServices', 'organixControllers']);

organixApp.config(['$routeProvider', function($routeProvider) {

	$routeProvider.
	when('/ui/configuration', { templateUrl: 'partials/config-list.html', controller: 'ConfigurationCtrl'}).
	when('/ui/configuration/objectTypes/:typeId', { templateUrl: 'partials/objectType-detail.html', controller: 'ObjectTypeDetailCtrl'}).
	when('/ui/configuration/connectionTypes/:typeId', { templateUrl: 'partials/connectionType-detail.html', controller: 'ConnectionTypeDetailCtrl'}).
	otherwise({redirectTo: '/ui/configuration'});

}]);
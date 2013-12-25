angular.module('SharedServices', [])
    .config(function ($httpProvider) {
        $httpProvider.responseInterceptors.push('myHttpInterceptor');
        var spinnerFunction = function (data, headersGetter) {
            // todo start the spinner here
       	$('.spinner').show();

//        	$("<div />").css({
//        	    position: "absolute",
//        	    width: "100%",
//        	    height: "100%",
//        	    left: 0,
//        	    top: 0,
//        	    zIndex: 1000000,  // to be on the safe side
//        	    background: "url(/img/organix-spinner.gif) no-repeat 50% 50%"
//        	}).appendTo($(".serverContent").css("position", "relative"));
        	
            return data;
        };
        $httpProvider.defaults.transformRequest.push(spinnerFunction);
    })
// register the interceptor as a service, intercepts ALL angular ajax http calls
    .factory('myHttpInterceptor', function ($q, $window) {
        return function (promise) {
            return promise.then(function (response) {
                // do something on success
                // todo hide the spinner
            	$('.spinner').hide();

                return response;

            }, function (response) {
                // do something on error
                // todo hide the spinner
            	$('.spinner').hide();
      
                return $q.reject(response);
            });
        };
    });

var organixApp = angular.module('organixApp', ['ngRoute', 'organixFilters', 'organixServices', 'organixControllers', 'SharedServices']);

organixApp.config(['$routeProvider', function($routeProvider) {

	$routeProvider.
	when('/ui/configuration', { templateUrl: 'partials/config-list.html', controller: 'ConfigurationCtrl'}).
	when('/ui/configuration/objectTypes/:typeId', { templateUrl: 'partials/objectType-detail.html', controller: 'ObjectTypeDetailCtrl'}).
	when('/ui/configuration/connectionTypes/:typeId', { templateUrl: 'partials/connectionType-detail.html', controller: 'ConnectionTypeDetailCtrl'}).
	otherwise({redirectTo: '/ui/configuration'});

}]);
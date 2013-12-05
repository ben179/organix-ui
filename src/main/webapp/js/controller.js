var organixControllers = angular.module('organixControllers', []);

organixControllers.controller('ConfigurationCtrl', function($scope, $http) {

	$scope.objectTypes = [];
	$scope.connectionTypes = [];
	$scope.orderPropObject = 'typeNumber';
	$scope.orderPropConnection = 'typeNumber';
	
	$http.get('/organix/objectType/').success(function(data) {
		$scope.objectTypes = data;
	});
	
	$http.get('/organix/connectionType/').success(function(data) {
		$scope.connectionTypes = data;
	});
	
	
	$scope.addObjectType = function(objectType) {    	
		
		$http.post('/organix/objectType/', objectType).
				success(function(data) {
					data.chosen = false;
					$scope.objectTypes.push(data);
					objectType.name='';
					objectType.typeNumber='';	
					$scope.objectErrorMessage = '';
				}).error(function(data,status,header,config){
					if (409 == status) {
						$scope.objectErrorMessage = data.organixModelErrorMessage;
					} else if (400 == status) {
						$scope.objectErrorMessage = data.fieldErrorsToString;		
					} else {
						$scope.objectErrorMessage = "Unknown error : " + status;
					}
				});

	};

	$scope.addConnectionType = function(connectionType) {    	
		
		$http.post('/organix/connectionType/', connectionType).
				success(function(data) {
					data.chosen = false;
					$scope.connectionTypes.push(data);
					connectionType.typeNumber='';	
					connectionType.sourceEnd.mandatory = false;
					connectionType.sourceEnd.unique = false;
					connectionType.sourceEnd.objectType = '';
					connectionType.sourceEnd.roleName = '';
					connectionType.targetEnd.mandatory = false;
					connectionType.targetEnd.unique = false;
					connectionType.targetEnd.objectType = '';
					connectionType.targetEnd.roleName = '';					
					$scope.connectionErrorMessage = '';
				}).error(function(data,status,header,config){
					if (409 == status) {
						$scope.connectionErrorMessage = data.organixModelErrorMessage;
					} else if (400 == status) {
						$scope.connectionErrorMessage = data.fieldErrorsToString;		
					} else {
						$scope.connectionErrorMessage = "Unknown error : " + status;
					}
				});

	};
	
});


organixControllers.controller('ObjectTypeDetailCtrl', function($scope, $routeParams) {

	$scope.objectTypeId = $routeParams.typeId;
	
});


organixControllers.controller('ConnectionTypeDetailCtrl', function($scope, $routeParams) {

	$scope.connectionTypeId = $routeParams.typeId;
	
});
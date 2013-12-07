var organixControllers = angular.module('organixControllers', []);

organixControllers.controller('ConfigurationCtrl', function($scope, $http) {

	$scope.objectTypes = [];
	$scope.connectionTypes = [];
	$scope.orderPropObject = 'typeNumber';
	$scope.reversePropObject = false;
	$scope.orderPropConnection = 'typeNumber';
	$scope.reversePropConnection = false;
	
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
					$scope.connectionErrorMessage = '';
					$scope.cleanConnectionType();
					$scope.typeToDeriveFrom = {};
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
	
	$scope.deriveConnectionType = function(typeToDeriveFrom) {

		if (typeToDeriveFrom != null) {
			$scope.connectionType.sourceEnd.objectType = typeToDeriveFrom.sourceEnd.objectType;
			$scope.connectionType.sourceEnd.roleName = typeToDeriveFrom.sourceEnd.roleName;
			$scope.connectionType.sourceEnd.mandatory = typeToDeriveFrom.sourceEnd.mandatory;
			$scope.connectionType.sourceEnd.unique = typeToDeriveFrom.sourceEnd.unique;
			
			$scope.connectionType.targetEnd.objectType = typeToDeriveFrom.targetEnd.objectType;
			$scope.connectionType.targetEnd.roleName = typeToDeriveFrom.targetEnd.roleName;
			$scope.connectionType.targetEnd.mandatory = typeToDeriveFrom.targetEnd.mandatory;
			$scope.connectionType.targetEnd.unique = typeToDeriveFrom.targetEnd.unique;			
		} else {
			$scope.cleanConnectionType();
		}
	};
	
	$scope.cleanConnectionType = function() {
		$scope.connectionType.typeNumber='';	
		$scope.connectionType.sourceEnd.mandatory = false;
		$scope.connectionType.sourceEnd.unique = false;
		$scope.connectionType.sourceEnd.objectType = '';
		$scope.connectionType.sourceEnd.roleName = '';
		$scope.connectionType.targetEnd.mandatory = false;
		$scope.connectionType.targetEnd.unique = false;
		$scope.connectionType.targetEnd.objectType = '';
		$scope.connectionType.targetEnd.roleName = '';	
	};
	
});


organixControllers.controller('ObjectTypeDetailCtrl', function($scope, $routeParams) {

	$scope.objectTypeId = $routeParams.typeId;
	
});


organixControllers.controller('ConnectionTypeDetailCtrl', function($scope, $routeParams) {

	$scope.connectionTypeId = $routeParams.typeId;
	
});
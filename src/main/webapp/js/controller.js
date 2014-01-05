var organixControllers = angular.module('organixControllers', []);

organixControllers.controller('ConfigurationCtrl', function($scope, $http) {
	


	
	   //============== DRAG & DROP =============
    // source for drag&drop: http://www.webappers.com/2011/09/28/drag-drop-file-upload-with-html5-javascript/
    var dropbox = document.getElementById("dropbox");
    $scope.dropText = 'Drop files here...';

    // init event handlers
    function dragEnterLeave(evt) {
        evt.stopPropagation();
        evt.preventDefault();
        $scope.$apply(function(){
            $scope.dropText = 'Drop files here...';
            $scope.dropClass = '';
        });
    }
    dropbox.addEventListener("dragenter", dragEnterLeave, false);
    dropbox.addEventListener("dragleave", dragEnterLeave, false);
    dropbox.addEventListener("dragover", function(evt) {
        evt.stopPropagation();
        evt.preventDefault();
        var clazz = 'not-available';
        var ok = evt.dataTransfer && evt.dataTransfer.types && evt.dataTransfer.types.indexOf('Files') >= 0;
        $scope.$apply(function(){
            $scope.dropText = ok ? 'Drop files here...' : 'Only files are allowed!';
            $scope.dropClass = ok ? 'over' : 'not-available';
        });
    }, false);
    dropbox.addEventListener("drop", function(evt) {
        console.log('drop evt:', JSON.parse(JSON.stringify(evt.dataTransfer)));
        evt.stopPropagation();
        evt.preventDefault();
        $scope.$apply(function(){
            $scope.dropText = 'Drop files here...';
            $scope.dropClass = '';
        });
        var files = evt.dataTransfer.files;
        if (files.length > 0) {
            $scope.$apply(function(){
                $scope.files = [];
                for (var i = 0; i < files.length; i++) {
                    $scope.files.push(files[i]);
                }
            });
        }
    }, false);
    //============== DRAG & DROP =============

    $scope.setFiles = function(element) {
    $scope.$apply(function($scope) {
      console.log('files:', element.files);
      // Turn the FileList object into an Array
        $scope.files = [];
        for (var i = 0; i < element.files.length; i++) {
          $scope.files.push(element.files[i]);
        }
      $scope.progressVisible = false;
      });
    };

    $scope.uploadFile = function() {
        var fd = new FormData();
        for (var i in $scope.files) {
            fd.append("ConfigFile", $scope.files[i]);
        }
        var xhr = new XMLHttpRequest();
        xhr.upload.addEventListener("progress", uploadProgress, false);
        xhr.addEventListener("load", uploadComplete, false);
        xhr.addEventListener("error", uploadFailed, false);
        xhr.addEventListener("abort", uploadCanceled, false);
        xhr.open("POST", "/organix/configuration/upload");
        $scope.progressVisible = true;
        xhr.send(fd);
    };

    function uploadProgress(evt) {
        $scope.$apply(function(){
            if (evt.lengthComputable) {
                $scope.progress = Math.round(evt.loaded * 100 / evt.total);
            } else {
                $scope.progress = 'unable to compute';
            }
        });
    };

    function uploadComplete(evt) {
        /* This event is raised when the server send back a response */
        alert(evt.target.responseText);
        
		$scope.configuration = evt.target.responseText;
		$scope.configurationUrl = '/organix/configuration/' + $scope.configuration.id;
		$scope.loadConfigHeaders();
		$scope.currentConfigurationHeader = evt.target.responseText.configuration.id;
    };

    function uploadFailed(evt) {
        alert("There was an error attempting to upload the file.");
    };

    function uploadCanceled(evt) {
        $scope.$apply(function(){
            $scope.progressVisible = false;
        });
        alert("The upload has been canceled by the user or the browser dropped the connection.");
    };
	
	$scope.newConfiguration = {};
	$scope.newConfiguration.version = 1;
	$scope.configurationsHeaders = null;
	$scope.configurationsExist = false;
	$scope.currentConfigurationHeader = null;
	$scope.configuration = {};
	$scope.configurationUrl = '';
	$scope.orderPropObject = 'typeNumber';
	$scope.reversePropObject = false;
	$scope.orderPropConnection = 'typeNumber';
	$scope.reversePropConnection = false;
    
	$scope.loadConfigHeaders = function() {
		
		$http({
			method: 'GET',
			url: '/organix/configuration',
			data: 'headersOnly=true',
			headers: {'Content-Type':'application/x-www-form-urlencoded; charset=UTF-8'} 
		}).success(function(data, status, headers, config) {
			$scope.configurationHeaders = data.configurationList;
			$scope.configurationsExist = data.configurationList.length > 0;
			$scope.newConfiguration = {};			
			$scope.newConfiguration.version = 1;
		});
		
	};

	$scope.uploadConfiguration = function(configuration) {
		
		$http({
			method: 'POST',
			url: '/organix/configuration',
			data: angular.toJson(configuration),
			headers: {'Content-Type': 'application/json'}
		}).success(function(data, status, headers, config){
			$scope.configuration = data;
			$scope.configurationUrl = headers('Location');
			$scope.loadConfigHeaders();
			$scope.currentConfigurationHeader = data.id;
		});
				
	};
	
	$scope.addObjectType = function(objectType) {    	
		
		$http.post($scope.configurationUrl + '/objectType/', objectType).
				success(function(data) {
					data.chosen = false;
					$scope.configuration.objectTypes.push(data.objectType);
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
		
		$http.post($scope.configurationUrl + '/connectionType/', connectionType).
				success(function(data) {
					data.chosen = false;
					$scope.configuration.connectionTypes.push(data.connectionType);					
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
	
	$scope.switchSourceAndTargetTypes = function() {
		var type = $scope.connectionType.targetEnd.objectType;
		$scope.connectionType.targetEnd.objectType = $scope.connectionType.sourceEnd.objectType;
		$scope.connectionType.sourceEnd.objectType = type;
	};
	
	$scope.loadConfiguration = function() {

		$http({
			method: 'GET',
			url: '/organix/configuration/' + $scope.currentConfigurationHeader,
			data: 'headersOnly=true',
			headers: {'Content-Type':'application/x-www-form-urlencoded'} 
		}).success(function(data, status, headers, config) {
			$scope.configuration = data;
			$scope.configurationUrl = headers('Location');
		});
	};

	$scope.importConfigFile = function(fileName) {
		 $http.get(fileName)
	       .then(function(res){
	    	   $scope.newConfiguration = res;                
	        });
		
	};
	
	$scope.loadConfigHeaders();
	
});


organixControllers.controller('ObjectTypeDetailCtrl', function($scope, $routeParams) {

	$scope.objectTypeId = $routeParams.typeId;
	
});


organixControllers.controller('ConnectionTypeDetailCtrl', function($scope, $routeParams) {

	$scope.connectionTypeId = $routeParams.typeId;
	
});
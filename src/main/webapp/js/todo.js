    function TodoCtrl($scope) {
    	$scope.todos = [
    		{typeName:'learn angular', typeNumber:1},
    		{typeName:'build an angular app', typeNumber:2}
    	];
     
    	$scope.addObjectType = function() {
    	
    	
    		$scope.todos.push({typeName:$scope.typeName, typeNumber:$scope.typeNumber});
    		$scope.typeName = '';
    		$scope.typeNumber = '';
    	};
     
    	$scope.remaining = function() {
    		var count = 0;
    		angular.forEach($scope.todos, function(todo) {
    			count += todo.done ? 0 : 1;
    		});
    		
    		return count;
    	};
     
    	$scope.archive = function() {
    		var oldTodos = $scope.todos;
    		$scope.todos = [];
    		angular.forEach(oldTodos, function(todo) {
    		if (!todo.done) $scope.todos.push(todo);
    	});
    	};}



<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
 
<!DOCTYPE HTML>
<html ng-app>
  <head>
    <title>Organix Configuration</title>
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.3/angular.min.js"></script>
    <script src="/js/todo.js"></script>
	<link rel="stylesheet" href="/css/todo.css">
    <style>
      body { background-color: #eee; font: helvetica; }
      #container { width: 500px; background-color: #fff; margin: 30px auto; padding: 30px; border-radius: 5px; box-shadow: 5px; }
      .green { font-weight: bold; color: green; }
      .message { margin-bottom: 10px; }
      label { width:70px; display:inline-block;}
      .hide { display: none; }
      .error { color: red; font-size: 0.8em; }
    </style>
  </head>
  <body>
   
  <div id="container">
   
    <h1>Organix Configuration Page</h1>


		<div ng-controller="TodoCtrl">
			<span>{{remaining()}} of {{todos.length}} remaining</span> 
			[ <a href="" ng-click="archive()">archive</a> ]
			<ul class="unstyled">
				<li ng-repeat="todo in todos">
					<input type="checkbox" ng-model="todo.done"> <span class="done-{{todo.done}}">{{todo.text}}</span>
				</li>
			</ul>
			<form ng-submit="addObjectType()">
				<input type="text" ng-model="typeName" size="30" placeholder="ObjectType name" /> 
				<input type="text" ng-model="typeNumber" size="20" placeholder="ObjectType number"/> 
				<input class="btn-primary" type="submit" value="Add new ObjectType">
			</form>
		</div>

		<h2>All Object Types</h2>
    <input type="submit" id="randomPerson" value="All Object Types" /><br/><br/>
    <div id="getAllObjectTypesResponse"> </div>
     
    <hr/>
     
    <h2>Get By ID</h2>
    <form id="idForm">
      <div class="error hide" id="idError">Please enter a valid Object Type ID</div>
      <label for="typeNumber">Object Type ID: </label><input name="id" id="typeNumber" value="0" type="number" />
      <input type="submit" value="Get ObjectType By ID" /> <br /><br/>
      <div id="objectTypeByIdResponse"> </div>
    </form>
     
    <hr/>
     
    <h2>Submit new Object Type</h2>
    <form id="newObjectType">
      <label for="typeNumber">Type Number: </label>
      <input type="text" name="typeNumber" id="numberInput" />
      <br/>
       
      <label for="name">Type Name: </label>
      <input type="text" name="name" id="nameInput" />
      <br/>
      <input type="submit" value="Save Object Type" /><br/><br/>
      <div id="objectTypeFormResponse" class="green"> </div>
    </form>
  </div>
   
   
  <script type="text/javascript">
   
    $(document).ready(function() {
       
      // Get All Object Types
      $('#randomPerson').click(function() {
        $.getJSON('${pageContext.request.contextPath}/organix/objectType', function(objectTypes) {
        	
        	var types = "";
        	for (i in objectTypes) {
        		types += objectTypes[i].name + " : " + objectTypes[i].typeNumber + " ";
        	}
        	
          $('#getAllObjectTypesResponse').text(types);
        });
      });
       
      // Get Obect type by type number
      $('#idForm').submit(function(e) {
        var personId = +$('#typeNumber').val();
        if(!validatePersonId(personId))
          return false;
        $.get('${pageContext.request.contextPath}/organix/objectType/' + personId, function(objectType) {
          $('#objectTypeByIdResponse').text(objectType.name + ', Type Number: ' + objectType.typeNumber);
        });
        e.preventDefault(); // prevent actual form submit
      });
       
      // Create New Object Type
      $('#newObjectType').submit(function(e) {
        // will pass the form data using the jQuery serialize function
        $.post('${pageContext.request.contextPath}/organix/objectType', $(this).serialize(), function(objectType) {
          $('#objectTypeFormResponse').text("Created: " + objectType.name + "(" + objectType.typeNumber + ")");
        });
         
        e.preventDefault(); // prevent actual form submit and page reload
      });
       
    });
     
    function validatePersonId(personId) {
      console.log(personId);
      if(personId === undefined || personId < 0 || personId > 3) {
        $('#idError').show();
        return false;
      }
      else {
        $('#idError').hide();
        return true;
      }
    }
     
   
  </script>
   
  </body>
</html>
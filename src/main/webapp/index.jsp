<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE HTML>
<html ng-app="organixApp">
<head>

<title>Organix Configuration</title>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script src="/lib/jquery-1.9.1.min.js"></script>
<script src="/lib/angular.min.js"></script>
<script src="/lib/angular-resource.min.js"></script>
<script src="/lib/angular-route.min.js"></script>
<script src="/js/app.js"></script>
<script src="/js/controller.js"></script>
<script src="/js/filters.js"></script>
<script src="js/services.js"></script>

<link rel="stylesheet" href="/css/style.css">

</head>

<body>

	<div id="container">
		<h1>Organix Configuration Page</h1>

		<hr />
		<div ng-view></div>
	</div>

</body>
</html>
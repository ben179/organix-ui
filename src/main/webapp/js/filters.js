
var organixFilters = angular.module('organixFilters', []);

organixFilters.filter('checkmark', function() {
	return function(input) {
		return input ? '\u2713' : '\u2718';
	};
});

organixFilters.filter('updown', function() {
	return function(input) {
		return input ? '\u2B06' : '\u2B07';
	};
});
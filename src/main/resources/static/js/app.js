angular.module('groupbyApp',
    ['ui.router',
     'ngResource',
     'groupbyApp.controllers',
     'ui.bootstrap']);

angular.module('groupbyApp')
    .config(function ($stateProvider) {
    $stateProvider.state('search', {
        url: '/search',
        templateUrl: 'partials/search.html',
        controller: 'SearchController',
        reloadOnSearch: false
    });
}).run(function ($state) {
    $state.go('search');
});
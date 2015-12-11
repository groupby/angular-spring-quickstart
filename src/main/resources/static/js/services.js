angular.module('groupbyApp.services',
[]).factory('GroupBySearch', function ($resource) {
    return $resource('/search?a=Angular&f=title,image&ps=10', {}, {
    query: {
        isArray: false
    }
    });
});
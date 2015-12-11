angular.module('groupbyApp.controllers', [
]).controller('SearchController', function ($q, $http, $scope, $state) {
    $scope.term = "laptop";
    $scope.noRecords = false;
    $scope.selected = [];

    $scope.addRefinement = function(navigation, refinement){
        $scope.selected.push(navigation.name + "=" + refinement.value);
        update();
    }
    $scope.removeRefinement = function(navigation, refinement){
        _.remove($scope.selected, function(n){ return n == navigation.name + "=" + refinement.value; });
        update();
    }
    $scope.$watch('term', function () {
        if ($scope.term.length > 0) {
            update();
        }
    }, true);

    var canceler;
    function update(){
        if (canceler) {
            canceler.resolve();
        }
        canceler = $q.defer();
        $http({url: '/search',
            params: { 'f': 'title,image',
                      'q':$scope.term,
                      'r':$scope.selected.join('~')},
            timeout: canceler.promise}).
            success(
                function(results) {
                    if (results.totalRecordCount > 0) {
                        $scope.results = results;
                        $scope.noRecords = false;
                    } else {
                        $scope.noRecords = true;
                    }
            });
    }
})
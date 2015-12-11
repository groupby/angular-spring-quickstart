Angular Spring Quickstart
===


A work in progress for a angular single page app for interacting with search and navigation against the GroupBy cloud application.
(Requires JDK 1.8)

Usage
---

### To run in development mode from the command line using maven

```bash
mvn spring-boot:run -DclientKey=<your client key> -DcustomerId=<your customer id>
```

You can override these settings at the command line:

```coffeescript
-Dcollection=docs
-Darea=Production
-DpageSize=10
-Dfields=title,image (defaults to title only)
```

All of these settings can be overridden in the angular controller HTTP request.

```coffeescript
params: { 'f': 'title,image',
          'q': $scope.term,
          'a': 'myArea',
          'c': 'myCollection',
          'p': 0,
          'ps': 50,
          'r':$scope.selected.join('~')}
```

### To create a self contained executable jar

```bash
mvn package
java -DcustomerId=<customerId> -DclientKey=<clientKey> -jar target/angular-spring-quickstart-*.jar
```

Additionally you can set the port to be something other than 8080 by appending `--server.port=8000`
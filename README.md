Angular Spring Quickstart
===


A work in progress for a angular single page app for interacting with search and navigation against the GroupBy cloud application.

Usage
---

### To run in development mode from the command line using maven

```bash
mvn spring-boot:run -DclientKey=<your client key> -DcustomerId=<your customer id>
```


### To create a self contained executable jar  (Requires JDK 1.8)

```bash
mvn package
java -DcustomerId=<customerId> -DclientKey=<clientKey> -jar target/angular-spring-quickstart-*.jar --server.port=8080
```

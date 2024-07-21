# Wishlist


This project is a simple wishlist API that allows you to create, update, delete and list products in a wishlist.

## Good Practices
* Clean Architecture
* BDD (Using Cucumber)

## Technologies 
* Cucumber
* Spring Boot
* Java 21
* TestContainers (For integration tests)

## Requirements
* Docker (For running the database and also to run Java TestContainers library for integration tests)
* Java 21
* Maven
* Docker Compose


## Running the application

### Initialize the database
```bash
docker-compose up -d
```

### Start the application
```bash
mvn spring-boot:run
```

## API Documentation

http://localhost:8080/wishlist/swagger-ui/index.html

## Running the tests

### Unit tests
```bash
mvn test
```

### Integration tests (BDD)
```bash
mvn test -Dsurefire.includeJUnit5Engines=cucumber -Dcucumber.features=src/test/resources/casetests -Dcucumber.plugin=pretty
```


## Architectural Decisions

### Clean Architecture
I decided to recreate the entities to `drivers.repositories` package to avoid coupling the entities from `entities` package with the database annotation particularities.

### Database mongo repository
I created two database classes, one using JPA queries and another one using mongo template for more complex queries.
The another one with more complex queries(using mongo template) could be built using less code, but I decided to delegate for the database layer all the queries operation (to improve performance), resulting in a bit more code.

### Wishlist API

Since the wishlist has a path like `/wishlist/customers/{customerId}/products/{productId}`, I decided to not validate the `customerId` to focus only in the wishlist feature.


## Tests

For tests, I decided to use a smaller value for the maximum amount of products in a wishlist, overriding the value in the `application.properties` file.
### Customer Service 

## Description

Customer service deals with zilch customer information
It can 
1. Get information of a customer given id 
2. Add new customer 
3. Update customer 
4. Delete customer 

What is Special? 
As Zilch customer information is accessed frequently we are using service level cache to store user details. 
We also update the cache data if customer present in cache. 

For more details run the application and see api documentation. 

## Teck stack used

* Java 17 
* Spring Boot 
* Spring boot redis
* H2 database 
* Junit5 + Mockito for unit and integration testing
* Lombok for code reduction
* Model validations using javax
* Open API - swagger 3 for REST API documentation
 


## How to get started

1. This application requires redis

``brew services restart redis ``

if not installed in your machine present, please download it 
``brew install redis``


2. mvn clean install 

3. swagger api path : http://localhost:8080/swagger-ui/index.html



#Accounts Demo
This is accounts demo, application developed as a part of exercise for Modulr Finance. 

##Various Packages and Classes in Application 
Services are in service package 
Controller in controller package. 
Repository are defined in repository package. 


ATMService - Interface defined 
ATMServiceImpl - Defines the implementation. 

AccountService - Interface defines accounts business
AccountServiceImpl - Defines account business. 

Account - Entity to be mapped to ACCOUNT table. 
Denomination - Entity to be mapped to DENOMINATION table. 

Properties defined within application.properties 

spring.h2.console.enabled=true
spring.h2.console.path=/h2

# Datasource
spring.datasource.url=jdbc:h2:file:~/test
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.h2.Driver


payments.denomination.size=100 - Default size for denomination. 
payments.withdraw.limit.minimum=50 - Define the minimum withdraw limit. 
payments.withdraw.limit.maximum=250 - Define the maximum withdraw limit. 



We are using the H2 database for this application.
When we start the AccountsDemoApplication, it initiates the LoadDatabase, which initialises the Accounts which were mentioned 
Also it initialises the Denomination table with appropriate ones. 

Postman is used for testing the REST API, especially various controller API. 
The postman collection is available within resources/others


##Unit Testing 
Unit Testing for Repository and Service classes is provided. 


##REST API Testing 
API for testing various behaviours is provided in the postman collection within resource folder. 
Load it into your postman and when you start the application 
The Application has Class LoadDatabase which would preload it with specific entries specified in the assignment.

#Assumption
1. Accounts are stored in the H2 Database. 
2. Currently authentication is not performed. One can implement OAUTH2.0 API and use Access tokens for same. 
3. Unit Testing for WebController is not done as Unit Testing of Service was primary focus. 
4. Postman was used for Testing API calls. # accountsdemo

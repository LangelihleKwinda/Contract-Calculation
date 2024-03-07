# Project Title

User Registration and Contract Calculation offer: Backend

## Getting Started

```
git clone git@github.com:LangelihleKwinda/Contract-Calculation.git
```


### Prerequisites

The things you need before installing the software.

* maven 3.6.X
* Java 17
* Open Api (installation)


### Installation

A step by step guide that will tell you how to get the development environment up and running.

#### Build application
``` mvn clean install```

#### Start application via embedded tomcat
``` mvn spring-boot:run```

# If build application startup fails because of failing tests (Shouldn't have, tested), run below command to skip tests
``` mvn spring-boot:run -Dspring-boot.run.profiles=LOCAL -DskipTests```


#### Api's
# You can hit the api's directly from Postman or an alternative tool


``` 
System health check: Check is application is up
Endpoint: http://localhost:9090/contractdemo/health/ping/v1

Register user: Saves a user in the database
Endpoint: http://localhost:9090/contractdemo/users/register/v1                                                                                                                   
Sample request file:  src/main/resources/data/registerUser.json

User Login:	User logins in after successful registration
Endpoint: http://localhost:9090/contractdemo/users/login/v1                                                                                                                   
Sample request file:     src/main/resources/data/userLogin.json

Search user: Verify is user has created	
Endpoint: http://localhost:9090/contractdemo/users/search/{username}/v1                                                                                                                    
Sample request:     src/main/resources/data/registerUser.json

Calculate repayment options:
Endpoint: http://localhost:9090/contractdemo/devices/calculateRepayments/{deviceAmount}/v1  
example: http://localhost:9090/contractdemo/devices/calculateRepayments/10000/v1                                                                                                                   

```


```
$ GENERAL
$ Application uses H2 database (in memory database)
$ Liquibase is used to create the databse table
$ All Spring Dependency Injections must use Constructor Injection
$ Application uses embedded tomcat server
$ User specific dto's are created when the application is buildt using the maven command (RegisterUserResponse, User, UserLoginRequest, UserRegistrationRequest, UserRegistrationResponse),
 this is defined on the yaml (src/main/resources/swagger/User_1_0.yaml)
$ Benfits of OpenApi: Documents your Api's, Generates swagger doc and provides request and response samples
$ DTO directory: target/classes/demo/api/User/V1

```

## Testing Conventions

Information Detailing Testing standards utilized in project.

```
$ Mockito is used to for unit tests
$ All test case assertions are be done using the Shazamcrest Library
$ Code Coverage for tests: src/main/resources/data/test code coverage.png

```
# User Authentication and Authorization Management

This repository contains code for managing user authentication and authorization in a web application.

## File Tree

```
.
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── fqrmix
│   │   │           └── authcenterback
│   │   │               ├── controllers
│   │   │               │   ├── AuthController.java
│   │   │               │   └── AuthorityController.java
│   │   │               ├── dto
│   │   │               │   ├── request
│   │   │               │   │   ├── AuthorityRequestDTO.java
│   │   │               │   │   ├── LoginRequestDTO.java
│   │   │               │   │   ├── RefreshRequestDTO.java
│   │   │               │   │   └── RegisterRequestDTO.java
│   │   │               │   └── response
│   │   │               │       ├── api
│   │   │               │       │   ├── impl
│   │   │               │       │   │   ├── ApiErrorResponseImpl.java
│   │   │               │       │   │   └── ApiSuccessResponseImpl.java
│   │   │               │       │   ├── ApiResponse.java
│   │   │               │       │   ├── ErrorResponse.java
│   │   │               │       │   └── SuccessResponse.java
│   │   │               │       └── data
│   │   │               │           ├── TokenResponse.java
│   │   │               │           └── UserDataResponse.java
│   │   │               ├── exception
│   │   │               │   └── UserAlreadyExistsException.java
│   │   │               ├── interceptors
│   │   │               │   ├── helpers
│   │   │               │   │   ├── SensitiveContentCachingRequestWrapper.java
│   │   │               │   │   └── SensitiveContentCachingResponseWrapper.java
│   │   │               │   ├── JwtAuthenticationErrorInterceptor.java
│   │   │               │   ├── JwtAuthenticationInterceptor.java
│   │   │               │   └── LoggingInterceptor.java
│   │   │               ├── models
│   │   │               │   ├── enums
│   │   │               │   │   ├── ERole.java
│   │   │               │   │   └── ErrorsConstants.java
│   │   │               │   ├── ErrorObject.java
│   │   │               │   ├── Role.java
│   │   │               │   └── User.java
│   │   │               ├── repositories
│   │   │               │   ├── RoleRepository.java
│   │   │               │   └── UserRepository.java
│   │   │               ├── security
│   │   │               │   └── SecurityConfiguration.java
│   │   │               ├── services
│   │   │               │   ├── impl
│   │   │               │   │   ├── UserDetailsImpl.java
│   │   │               │   │   └── UserDetailsServiceImpl.java
│   │   │               │   ├── AuthenticationService.java
│   │   │               │   ├── JWTService.java
│   │   │               │   ├── ModulesService.java
│   │   │               │   └── MyUserDetailsService.java
│   │   │               ├── utils
│   │   │               │   └── GlobalRestExceptionHandler.java
│   │   │               └── AuthCenterBackApplication.java
│   │   └── resources
│   │       └── application.properties
│   └── test
│       └── java
│           └── com
│               └── fqrmix
│                   └── authcenterback
│                       └── AuthCenterBackApplicationTests.java
├── Dockerfile
├── README.md
├── build.gradle
├── docker-compose.yml
├── gradlew
├── gradlew.bat
└── settings.gradle

```

## JWTService

The `JWTService` class is responsible for generating and verifying JSON Web Tokens (JWT). It contains methods for creating a JWT token based on user authentication and for validating and extracting information from JWT tokens.

## AuthenticationService

`AuthenticationService` represents a service for performing user authentication and registration operations. It contains methods for user login and registration.

## UserDetailsImpl

The `UserDetailsImpl` class implements the `UserDetails` interface and represents user details required for authentication. It contains information about the user, their roles, and authorities.

## UserDetailsServiceImpl

`UserDetailsServiceImpl` implements the `MyUserDetailsService` interface and provides a service for working with users. This class implements methods for saving, creating, and loading user information from the database.

## LoginRequestDTO

The `LoginRequestDTO` class is a Data Transfer Object (DTO) for an authentication request. It contains fields for the username and password.

## SecurityConfiguration

`SecurityConfiguration` contains the security configuration of the application. It defines authentication, authorization, and JWT token handling settings.

## AuthEntryPointJwt

`AuthEntryPointJwt` is a component responsible for handling JWT authentication errors. It handles sending an appropriate response in case of authentication errors.

## AuthTokenFilter

The `AuthTokenFilter` class is a filter for processing JWT tokens. It verifies and authenticates users based on the provided tokens.

## LoggingFilterBean

`LoggingFilter` is responsible for logging HTTP requests and responses. It records information about requests and responses for analysis and debugging.

## User

`User` represents a user entity. It contains information about the user such as username, password, and a set of provided services.

Each class in the repository performs a specific function in the process of user authentication and authorization. They interact to ensure security and access control in the web application.

Food Delivery Platform - Centralized Configuration (Spring Cloud Config)

This repository now includes a Spring Cloud Config Server to centralize configuration for all microservices.

Modules added
- config-server (port 8888)
- shared-config (local file-based Git-less config repository used by the server in native mode)

How to run (local dev)
1) Start Discovery Server (Eureka)
   - cd discovery-server
   - mvn spring-boot:run
   - UI: http://localhost:8761/

2) Start Config Server
   - cd config-server
   - mvn spring-boot:run
   - Health: http://localhost:8888/actuator/health
   - Sample config endpoint: http://localhost:8888/auth-service/default

3) Start microservices (auth-service, cart-service, restaurant-service, gateway-server)

Using centralized config in services
- With Spring Boot 3.x and Spring Cloud 2023.x, add this to each service application.yml:

  spring:
    application:
      name: <service-name>
    config:
      import: optional:configserver:http://localhost:8888

- Also add dependency to each service pom.xml:

  <dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
  </dependency>

Included central properties
- shared-config/application.yml: common defaults
- shared-config/auth-service.yml: JWT secret and expiration

Notes
- In production, point the config server to a Git repository instead of native mode.
- To refresh configs at runtime, include spring-boot-actuator and call /actuator/refresh (if using Spring Cloud Bus or enabling refresh scope where needed).

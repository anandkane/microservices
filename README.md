# microservices
Experiment with Microservices patterns

## Implementation of the following patterns
- Event Sourcing
- CQRS
- Saga
- API Gateway
- Service Discovery

## Technologies
These patterns are implemented using the following technologies
- Spring Boot
- Axon Framework AxonServer-2024.0.2 (https://axoniq.io/) for Event Sourcing and CQRS
- Axon Framework AxonServer-2024.0.2 (https://axoniq.io/) for Saga

## How to run
- Install and run AxonServer-2024.0.2
- Clone the repository
- Build the project using `mvn clean install`
- Start the in the below order
    - Eureka service
    - Api Gateway
    - Business services in any order


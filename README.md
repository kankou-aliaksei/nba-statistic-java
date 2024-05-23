# NBA Statistic

The microservice-based system for NBA player statistics is designed to seamlessly log and aggregate player and team performance data. By ensuring high availability and scalability, the system can handle large volumes of concurrent data inputs and deliver real-time, accurate statistics.

# Running the Application

## Prerequisites
- Docker
- Docker Compose

## Building and Running the Application

To build and start the application, run the following command:

```bash
docker-compose up --build
```

## Testing the API

You can test the API by sending a GET request to the following endpoint:

```
GET http://localhost:8080/api/players
```

# Technical Stack Overview

## Java 21
The choice of Java 21, the latest stable version, ensures the use of the latest language features and improvements, which enhance performance, security, and maintainability.

## Spring Boot with WebFlux and Project Reactor
Java, combined with Spring Boot, provides a robust framework for building microservices. WebFlux and Project Reactor enable reactive programming, which is well-suited for handling large numbers of concurrent requests efficiently. This is crucial for ensuring high availability and scalability.

## R2DBC (Reactive Relational Database Connectivity)
R2DBC allows for non-blocking interaction with the relational database, which fits well with the reactive programming model of WebFlux.

## PostgreSQL
Chosen for its mature, stable, and well-supported R2DBC driver, which ensures efficient and reliable non-blocking interactions in a reactive programming model. Its advanced features and robust performance make it ideal for handling complex, high-concurrency workloads.

## Docker Compose
This tool simplifies the management of multi-container Docker applications, making it easier to set up and maintain development environments.

## Docker
Ensuring consistency across different environments and simplifying the deployment process. The Dockerfile includes a build stage to streamline the build process, enhancing convenience to test the app.

## SQL Aggregation
Performed on the database side to leverage PostgreSQL's powerful querying capabilities, ensuring efficient and accurate computation of aggregate statistics. This reduces the load on the backend and improves overall system performance compared to doing aggregations on the backend side.

## Microservices Architecture
The microservices are designed to be stateless, allowing them to be scaled horizontally. This ensures that the system can handle increased load by adding more instances, maintaining high availability and performance.

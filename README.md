# 🧺 E-commerce Platform With Microservice Architecture

<p align="center">
  <img src="docs/images/guide-banner.png" alt="Microservice E-commerce Platform — From Zero to Production"/>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring Boot-3-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring Cloud-2023-6DB33F?style=for-the-badge&logo=spring&logoColor=white"/>
  <img src="https://img.shields.io/badge/Kubernetes-326CE5?style=for-the-badge&logo=kubernetes&logoColor=white"/>
  <img src="https://img.shields.io/badge/Terraform-7B42BC?style=for-the-badge&logo=terraform&logoColor=white"/>
  <img src="https://img.shields.io/badge/AWS EKS-FF9900?style=for-the-badge&logo=amazon-aws&logoColor=white"/>
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"/>
  <img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white"/>
  <img src="https://img.shields.io/badge/RabbitMQ-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white"/>
  <img src="https://img.shields.io/badge/Keycloak-4D4D4D?style=for-the-badge&logo=keycloak&logoColor=white"/>
  <img src="https://img.shields.io/badge/Prometheus-E6522C?style=for-the-badge&logo=prometheus&logoColor=white"/>
  <img src="https://img.shields.io/badge/Grafana-F46800?style=for-the-badge&logo=grafana&logoColor=white"/>
</p>

<p align="center">
  <img src="https://img.shields.io/github/stars/miliariadnane/demo-microservices?style=for-the-badge"/>
  <img src="https://img.shields.io/github/forks/miliariadnane/demo-microservices?style=for-the-badge"/>
  <img src="https://img.shields.io/static/v1?label=%F0%9F%8C%9F&message=Star%20If%20Useful&style=for-the-badge&color=BC4E99"/>
</p>

> A practical e-commerce platform with Spring frameworks, Kubernetes, Terraform and deployed on AWS EKS. The repository demonstrates microservice patterns and cloud-native capabilities with focus on scalability, security, resiliency, observability, infrastructure automation and deployment improvements.

💡 This application is not business oriented and my focus is mostly on technical part, I just want to implement a sample app from scratch with microservice architecture using different technologies, principles and patterns.

🌀 This Application is `in-progress` and I will add new features over time. 🌀

## Guide & Documentation

Link to the documentation and guide website : [demo-microservices](https://miliariadnane.gitbook.io/demo-microservices) 

#### Table of Contents
```text
├── Overview
├── Set up a Microservice Project from Scratch
├── Microservices 101
│   ├── Service Discovery with Netflix Eureka
│   ├── Communication Between Microservices
│   │   ├── Synchronous Communication
│   │   └── Asynchronous Communication
│   ├── API Gateway
│   │   ├── Load Balancing
│   │   ├── Resiliency
│   │   │   ├── Circuit Breaker
│   │   │   ├── Fallback Pattern
│   │   │   └── Rate Limiting
│   │   └── Security
│   │       ├── API Key Manager
│   │       └── Authentication
│   ├── Cloud Native Deployment
│   │   ├── Containerization with Docker & Jib
│   │   ├── Container Orchestration with Kubernetes
│   │   └── Automating Deployment with Skaffold
│   └── Monitoring
│       ├── Distributed Tracing with Sleuth & Zipkin
│       └── Prometheus & Grafana
├── Microservices 102
│   ├── Service Discovery Deep Dive
│   ├── CQRS Pattern
│   ├── Cloud Deployment with AWS
│   ├── Infrastructure as Code with Terraform
│   │   ├── Terraform Fundamentals
│   │   ├── Our Terraform Configuration
│   │   └── Deploying to AWS with Terraform
│   └── Deployment Strategies
│       ├── Blue/Green Deployment (Docker Compose)
│       ├── Rolling Update (Kubernetes)
│       └── Canary Deployment (Kubernetes)
```

## Support

Show your support by:

- ⭐ Starring this repository. And we will be happy together :)
- 🤲 Making a duaa for me and my parents
- 🐛️ Reporting bugs or submitting pull requests to fix them
- 📢️ Suggesting new features through issues or pull requests

## Features
- ✅ `Service Discovery` with **Netflix Eureka**
- ✅ `API Gateway` with **Spring Cloud Gateway**
- ✅ `Distributed Tracing` using **Sleuth & Zipkin**
- ✅ `Event-Driven Architecture` with **RabbitMQ**
- ✅ `Security` with **API Key Manager** & **OAuth2/Keycloak**
- ✅ `Resiliency` with **Resilience4j** (Circuit Breaker, Fallback, Rate Limiting)
- ✅ Database per Service Pattern
- ✅ Cloud Native Support **(Kubernetes & AWS EKS)**
- ✅ `Infrastructure as Code` with **Terraform**
- ✅ `Automated Deployment` with **Skaffold**
- ✅ `Monitoring` with **Prometheus & Grafana**
- ✅ `Email Notifications` with **AWS SES**
- ✅ `Deployment Strategies` — **Blue/Green**, **Rolling Update**, **Canary**
- ✅ `CQRS Pattern` — separate read/write models with denormalized **Order View** read model

## Roadmap
- [x] Kubernetes Deployment
- [x] AWS EKS Integration
- [x] API Documentation with OpenAPI
- [x] Api Key Manager for APIs security
- [x] OAuth2 Authentication with Keycloak
- [x] Resilience4j for circuit breaker and fallback
- [x] Automated K8S Deployment with Skaffold
- [x] Infrastructure as Code with Terraform
- [x] Deployment Strategies (Blue/Green, Rolling Update, Canary)
- [x] CQRS Pattern with denormalized read model in Order Service
- [ ] Service Mesh Implementation
- [ ] Frontend application with `React` or `Angular`

## Technologies - Libraries

### Core

- ✔️ **[`eureka-server-discovery`](https://spring.io/guides/gs/service-registration-and-discovery/)** - Eureka is a service registry for resilient spring microservices.
- ✔️ **[`spring-cloud-gateway`](https://cloud.spring.io/spring-cloud-gateway/reference/html/)** - Spring Cloud Gateway is a non-blocking, reactive, based on Spring 5, web server gateway.
- ✔️ **[`spring-cloud-loadbalancer`](https://spring.io/guides/gs/spring-cloud-loadbalancer/)** - Spring Cloud LoadBalancer is a library that provides a common abstraction over client-side load balancing.
- ✔️ **[`sleuth-zipkin`](https://cloud.spring.io/spring-cloud-sleuth/reference/html/)** - Distributed tracing with Zipkin and Spring Cloud Sleuth.
- ✔️ **[`spring-boot-starter-data-jpa`](https://spring.io/projects/spring-data-jpa)** - Spring Data JPA is a layer on top of the JPA API.
- ✔️ **[`amqp-starter`](https://spring.io/projects/spring-amqp)** - Spring AMQP provides an abstraction layer for sending and receiving messages with a message broker.
- ✔️ **[`rabbitmq`](https://www.rabbitmq.com/)** - RabbitMQ is an open source message broker software that implements the Advanced Message Queuing Protocol (AMQP).
- ✔️ **[`Junit5`](https://junit.org/junit5/)** - For unit testing and integration testing.
- ✔️ **[`Mockito`](https://site.mockito.org/)** - For mocking objects in unit tests.

### Libraries

- ✔️ **[`mapstruct`](https://mapstruct.org/)** - MapStruct is a code generator that greatly simplifies the implementation of mappings between Java bean types based on a convention over configuration approach.
- ✔️ **[`open-feign`](https://cloud.spring.io/spring-cloud-openfeign/reference/html/)** - Declarative REST Client for spring.
- ✔️ **[`lombok`](https://projectlombok.org/)** - Project Lombok is a java library that automatically plugs into your editor and build tools, spicing up your java.
- ✔️ **[`micrometer`](https://micrometer.io/)** - Micrometer provides a simple facade over the instrumentation clients for the most popular monitoring systems, allowing you to instrument your JVM-based application code without vendor lock-in.
- ✔️ **[`jib-plugin`](https://github.com/GoogleContainerTools/jib)** - Container image builder that facilitates building container images for your Java applications.

### Infrastructure

- ✔️ **[`prometheus`](https://prometheus.io/)** - Prometheus is an open-source systems monitoring and alerting toolkit.
- ✔️ **[`grafana`](https://grafana.com/)** - Grafana is an open source, feature rich metrics dashboard and graph editor for Graphite, Elasticsearch, OpenTSDB, Prometheus and InfluxDB.
- ✔️ **[`AWS SES`](https://aws.amazon.com/ses/)** - Amazon Simple Email Service (Amazon SES) is a cloud-based email sending service.
- ✔️ **[`AWS EKS`](https://aws.amazon.com/eks/)** - Amazon Elastic Kubernetes Service (Amazon EKS) is a managed service that makes it easy for you to run Kubernetes on AWS without needing to install, operate, and maintain your own Kubernetes control plane.
- ✔️ **[`Terraform`](https://www.terraform.io/)** - Infrastructure as Code tool for provisioning and managing cloud infrastructure using declarative configuration files.
- ✔️ **[`Skaffold`](https://skaffold.dev/)** - Skaffold handles the workflow for building, pushing and deploying Kubernetes applications, enabling continuous development.
- ✔️ **[`Keycloak`](https://www.keycloak.org/)** - Open Source Identity and Access Management that adds security to your applications.
- ✔️ **[`Resilience4j`](https://resilience4j.readme.io/)** - Lightweight fault tolerance library for Java providing circuit breaker, rate limiter, retry and fallback patterns.
- ✔️ **[`Docker`](https://www.docker.com/)** - Docker is a set of platform as a service products that allows to build, test, and deploy applications quickly using containers.
- ✔️ **[`kubernetes`](https://kubernetes.io/)** - Kubernetes is an open-source system for automating deployment, scaling, and management of containerized applications.

## System Architecture

### Microservices Architecture

![](/docs/images/architecture-diagram.jpeg)

### Infrastructure Architecture

![](/docs/images/infrastructure-diagram.png)

### Deployment Architecture

![](/docs/images/deploy-workflow-diagram.png)

## License
This project is made available under the MIT license. See [LICENSE](https://github.com/miliariadnane/advanced-microservices/blob/main/LICENSE) for details.


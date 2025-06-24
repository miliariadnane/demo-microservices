# 🧺 E-commerce Platform With Microservice Architecture 🧺

<p align="center">
   <img src="https://img.shields.io/badge/Dev-miliariadnane-blue?style"/>
   <img src="https://img.shields.io/badge/language-java-red?style"/>
   <img src="https://img.shields.io/badge/Framework-Spring Frameworks-green?style"/>
   <img src="https://img.shields.io/github/stars/miliariadnane/demo-microservices"/>
   <img src="https://img.shields.io/github/forks/miliariadnane/demo-microservices"/>
   <img src="https://img.shields.io/static/v1?label=%F0%9F%8C%9F&message=If%20Useful&style=style=flat&color=BC4E99"/>
</p>

> A practical e-commerce platform with spring frameworks, kubernetes and deployed on AWS. The repository demonstrates microservice patterns and cloud-native capabilities with focus on scalability, security, resiliency, observability and deployment improvements.

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
│   │   └── Security
│   │       ├── API Key Manager
│   │       └── Authentication
│   ├── Cloud Native Deployment
│   │   ├── Containerization with Docker & Jib
│   │   └── Container Orchestration with Kubernetes
│   └── Monitoring
│       ├── Distributed Tracing with Sleuth & Zipkin
│       └── Prometheus & Grafana
└── Microservices 102
    ├── Service Discovery Deep Dive
    └── Cloud Deployment with AWS
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
- ✅ Database per Service Pattern
- ✅ Cloud Native Support **(Kubernetes & AWS)**
- ✅ `Monitoring` with **Prometheus & Grafana**
- ✅ `Email Notifications` with **AWS SES**

## Roadmap
- [x] Kubernetes Deployment
- [x] AWS EKS Integration
- [x] API Documentation with OpenAPI
- [x] Api Key Manager for APIs security
- [x] AWS EKS Integration
- [x] OAuth2 Authentication with Keycloak
- [x] Resilience4j for circuit breaker and fallback 🚧
- [x] Automated K8S Deployment with Skaffold 🚧
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
- ✔️ **[`Keycloak`](https://www.keycloak.org/)** - Open Source Identity and Access Management that adds security to your applications.
- ✔️ **[`Docker`](https://www.docker.com/)** - Docker is a set of platform as a service products that allows to build, test, and deploy applications quickly using containers.
- ✔️ **[`kubernetes`](https://kubernetes.io/)** - Kubernetes is an open-source system for automating deployment, scaling, and management of containerized applications.

## System Architecture

### Microservices Architecture

![](/docs/diagrams/architecture-diagram.png)

### Infrastructure Architecture

![](/docs/diagrams/infrastructure-diagram.png)

### Deployment Architecture

![](/docs/diagrams/deploy-workflow-diagram.png)

## License
This project is made available under the MIT license. See [LICENSE](https://github.com/miliariadnane/advanced-microservices/blob/main/LICENSE) for details.


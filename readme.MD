<p align="center">
   <img src="https://img.shields.io/badge/Dev-miliariadnane-blue?style"/>
   <img src="https://img.shields.io/badge/language-java-red?style"/>
   <img src="https://img.shields.io/badge/Framework-Spring Frameworks-green?style"/>
   <img src="https://img.shields.io/github/stars/miliariadnane/demo-microservices"/>
   <img src="https://img.shields.io/github/forks/miliariadnane/demo-microservices"/>
   <img src="https://img.shields.io/static/v1?label=%F0%9F%8C%9F&message=If%20Useful&style=style=flat&color=BC4E99"/>
</p>

## Overview

This repo contains demo about how to build simple ecommerce microservices app from scratch **`step by step`** based on spring boot (spring cloud, spring cloud gateway, spring data JPA, spring web, ...), deployed on Kubernetes and AWS using EKS.

The content part explains some concepts in microservices architecture like : service discovery, load balancing, distributed tracing, massaging & Advanced Message Queuing Protocol, ...

The app composed of 5 microservices [customer - product - order - payment - notification] communicating with each other using REST API (+ Open Feign) and messaging system (RabbitMQ).

Over the demonstration and documentation, we will cover a bunch of concepts in the microservice architecture :
   
 - Service Discovery
 - Traffic management : Spring Cloud Gateway - Zipkin 
 - Resiliency : Load balancing strategy 
 - Observability : Spring Boot Actuator - Prometheus - Grafana - Tracing

## Diagrams

* Architecture diagram :

![Diagram 2](docs/diagrams/architecture-diagram.png)

* Deployment diagram :

![Diagram 3](docs/diagrams/deployment-diagram.png)

## Content

- [0. Setup parent Module](#0-setup-parent-module)
- [1. Setup first microservice](#1-setup-first-microservice)
- [2. Postgres & PGAdmin on docker](#2-postgres--pgadmin-on-docker)
- [3. Create new microservices](#3-create-new-microservices)
- [4. Migrate from modelMapper to mapStruct](#4-migrate-from-modelmapper-to-mapstruct)
- [5. Communication between microservices using restTemplate](#5-communication-between-microservices-using-restTemplate)
- [6. Eureka Service Discovery](#6-eureka-service-discovery)
  - [1. What is service discovery](#1-what-is-service-discovery)
  - [2. How to use eureka server](#2-how-to-use-eureka-server)
- [7. Migrate From restTemplate to OpenFeign](#7-migrate-from-restTemplate-to-openFeign)
  - [1. Why OpenFeign](#1-why-openFeign)
  - [2. Steps to configure OpenFeign](#2-steps-to-configure-openFeign)
  - [3. Diagram of the communication between microservices](#3-diagram-of-the-communication-between-microservices)
- [8. Load Balancing concept](#8-load-balancing-concept)
- [9. Spring Cloud Gateway](#9-spring-cloud-gateway)
  - [1. What is Spring Cloud Gateway](#1-what-is-spring-cloud-gateway)
  - [2. Setup Spring Cloud Gateway](#2-setup-spring-cloud-gateway)
- [10. Distributed Tracing Sleuth & Zipkin](#10-distributed-tracing-sleuth--zipkin)
  - [1. What is Sleuth Zipkin](#1-what-is-sleuth-zipkin)
  - [2. Setup Zipkin](#2-setup-zipkin)
- [11. Notification with RabbitMQ](#11-notification-with-rabbitmq)
  - [1. Why a message broker](#1-why-a-message-broker)
  - [2. AMQP](#2-amqp)
  - [3. Setup RabbitMQ](#3-setup-rabbitmq)
- [12. Feign clients module](#12-feign-clients-module)
- [13. Containerizing microservices using JIB maven plugin](#13-containerizing-microservices-using-jib-maven-plugin)
  - [1. What is JIB](#1-what-is-jib)
  - [2. Setup JIB](#2-setup-jib)
- [14. Deploy microservices to local Kubernetes](#14-deploy-microservices-to-local-kubernetes)
  - [1. What is Kubernetes](#1-what-is-kubernetes)
  - [2. What is Deployment](#2-what-is-deployment)
  - [3. What are Services in k8s](#3-what-are-services-in-k8s)
  - [4. Setup Kubernetes](#4-setup-kubernetes)
  - [5. Deploy Postgres RabbitMQ & Zipkin to K8S](#5-deploy-postgres-rabbitmq--zipkin-to-k8s)
  - [6. No need for Gateway anymore](#6-no-need-for-gateway-anymore)
  - [7. No need for Service Discovery also](#7-no-need-for-service-discovery-also)
  - [8. Ingress](#8-ingress)
- [15. Configure AWS EKS cluster](#15-configuring-aws-eks-cluster)
  - [1. What is EKS](#1-what-is-eks)
  - [2. Setup EKS (solution 1)]()
  - [3. Setup EKS (solution 2)]()
  - [4. Add eks application config file for each microservice](#4-add-eks-application-config-file-for-each-microservice)
- [16. create databases on AWS RDS](#16-create-databases-on-aws-rds)
- [17. Monitoring microservices with Prometheus and Grafana](#17-monitoring-microservices-with-prometheus-and-grafana)
  - [1. Why monitoring](#1-why-monitoring)
  - [2. What is Micrometer](#2-whats-micrometer)
  - [3. Setup monitoring configuration](#3-setup-monitoring-configuration)
- [18. deploy microservices to EKS cluster using git actions](#18-deploy-microservices-to-eks-cluster-using-git-actions)
  - [1. Create deploy workflow](#1-create-deploy-workflow)
  - [2. Changes application yaml files](#2-changes-application-yaml-files)
  - [3. Add database username & password to deployment.yaml file as secrets](#3-add-database-username--password-to-deploymentyaml-file-as-secrets)
  - [4. Add secrets to eks cluster using command line locally](#4-add-secrets-to-eks-cluster-using-command-line-locally)
- [19. Deployment workflow diagram](#19-deployment-workflow-diagram)

## 0. Setup parent Module

### Maven command to generate the project :
```
mvn archetype:generate -DgroupId=dev.nano -DartifactId=ecomicroservices -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false
```

## 1. Setup first microservice

### Generate new module for customer microservices

   In order to create microservices instances in top of the parent module, we need to generate submodule for each microservice then we add the necessary dependencies.

   1. Add maven dependencies for customer submodule in **_pom.xml_** : 
      * Spring data JPA
      * Spring Web
      * POSTGRES JDBC Driver
      * Hibernate Validator
   2. Add classes [controller - service - repository] and resources.
   3. Add customized banner in resource folder.
   4. Configure database connection in **`application.yml`** file.

## 2. Postgres & PGAdmin on docker

1. Configure docker-compose file by adding Postgres and PGAdmin (GUI) images.
2. Connecting to DB using PGAdmin

## 3. Create new microservices

* Create new submodules for each microservice (same thing as on customer microservice) : product, order, payment and notification.

## 4. Migrate from  **`modelMapper`** to **`mapStruct`** 

  In the beginning, I have used modelMapper to map entities to DTOs and vice versa, but I have found that mapStruct is more efficient and faster than modelMapper. That's why I decided to move to mapStruct.

  * `mapStruct` is a library that helps to map objects between two classes, same as `modelMapper`.
  * `mapStruct` is more flexible and faster than `modelMapper` and it is easier to use.
  * Some resources explain why mapStruct is more performant than modelMapper :
    * https://www.baeldung.com/java-performance-mapping-frameworks
    * https://github.com/arey/java-object-mapper-benchmark

## 5. Communication between microservices using **`restTemplate`**

After creating all microservices, we're supposed to send requests (Http requests) between them via `restTemplate` to get data from other microservices.

1. Make a configuration file for restTemplate
2. Add restTemplate in the service class using dependency injection
3. Call the microservice using restTemplate

```
#Example in customer microservice

OrdersResponse ordersResponse = restTemplate.postForObject(
    "http://localhost:8003/api/v1/orders",
    customerOrdersRequest,
    OrdersResponse.class
);
```

## 6. Eureka Service Discovery 

### 1. What is service discovery 

   * `Service discovery` is a tool that helps for detecting the devices & services that are running on the network, in order to connect to them.
   * So Service Discovery by the end knows all the client applications running on each port & IP address.
   * Steps :
     * microservices register to eureka server.
     * look up the service using eureka server.
     * eureka server will return the service information.

   ![eureka-service-discovery](docs/images/eureka-service-discovery.jpeg)

   [Medium Photo By Isuru Jayakantha](https://medium.com/@ijayakantha/microservices-service-registration-and-discovery-with-netflix-eureka-9a2aa729da96)
   

### 2. How to use eureka server 

   * Add new module for eureka server (Server Side)
   * Add dependency for eureka in **`pom.xml`** for all microservices.
    
```yaml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

  * Add the configuration in **application.yml** file
  * Enable eureka in microservices using **`@EnableEurekaClient`** annotation in Application class.
  * You can check eureka dashboard with the current instances registered with Eureka on : http://localhost:8761
  * In case we would change the url of the service (here I'm talking about the call of microservice using restTemplate) by its name (the name in application yaml file) :
    * Here we should add a new annotation for restTemplate : **`@LoadBalanced`**
    * This annotation will help to load balance the call to the service, because it will use the name of the service without port number that define the instance.

## 7. Migrate From **`restTemplate`** to **`OpenFeign`** 

### 1. Why OpenFeign 

   * `OpenFeign` is a client-side library that simplifies the use of RESTful web services.
   * `OpenFeign` gives more flexibility to use APIs from different microservices just creating interfaces (CustomerOrdersFeignClient & CustomerProductFeignClient) that target the method body in the controller.

### 2. Steps to configure OpenFeign 
   * Add maven dependency on the parent module (pom.xml) :
    
```yaml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

  * Enable OpenFeign on microservice using **`@EnableFeignClients`** annotation in Application class.
  * Create interfaces for clients (CustomerOrdersFeignClient & CustomerProductFeignClient)
  * Replace restTemplate with OpenFeign in the service class.

### 3. Diagram of the communication between microservices 

  * Use case : Customer and Payment communication via Eureka Server
    
  ![eureka-service-discovery](docs/diagrams/communication-between-microservices.png)

## 8. Load Balancing concept

  * **`_Load balancing_`** is the process of distributing network traffic across multiple servers, to ensures no single server bears too much demand.
  * There is a variety of load balancing methods (_**that decide in which microservice instance to send the request**_), which use different algorithms best suited for a particular situation:

      * **Round Robin Method** — Requests are distributed across the group of servers sequentially.
      * **Least Connection Method** — directs traffic to the server with the fewest active connections
      * **Least Response Time Method** — Sends requests to the server selected by a formula that calculates the fastest response time & fewest active connections.
      * **IP Hash** — the IP address of the client determines which server receives the request.
      * ect

  * In the LB we manage a lot of things like : Authentication, High availability, Logging, caching, TLS, etc.
  * But in production we don't want to manage all these things, we can use some providers (aws, azure, google, etc.)

  * There's another concept in LB called **`Health Check`**, which is used to check the availability of the service to respond to the requests.

  * Some resources explain LB :

    * https://www.nginx.com/resources/glossary/load-balancing/
    * https://avinetworks.com/what-is-load-balancing/

## 9. Spring Cloud Gateway

### 1. What is Spring Cloud Gateway 

   * `Spring Cloud Gateway` is a service that allows you to route requests to different microservices. <br> It's help in **traffic management** (redirecting traffic), **load balancing**, **circuit breaker**, etc
   * Locally we will adopt API Gateway because it's need to configure a bunch of stuff. But in production we will use some providers (aws, azure, google, etc.) based on their features of load balancing.

### 2. Setup Spring Cloud Gateway 

   * Create new module for Spring Cloud Gateway.
   * Add maven dependency for Spring Cloud Gateway in **`pom.xml`** :
    
```yaml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
```

  * Set up the routes in **`application.yml`** file.
  * So at this step, instead of sending request to customer on : "http://localhost:**`8001`**/api/v1/customers", <br/> we will send request on tge port of api-gateway : "http://localhost:**`8004`**`/api/v1/customers".

## 10. Distributed Tracing Sleuth & Zipkin

### 1. What is Sleuth Zipkin 

   * `Sleuth Zipkin` is a distributed tracing library that allows you to trace the flow of requests and responses between microservices.
   * Furthermore, distributed tracing like zipkin help us to **debug our microservices** (if there's any latency or errors).

### 2. Setup Zipkin 

   * Add Zipkin dependency in all microservices over **`pom.xml`** :
    
```yaml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-sleuth-zipkin</artifactId>
</dependency>
```
   * Add config to application yml files (Example in product microservice) :
    
```yaml
#Spring
spring:
  application:
    name: product
  zipkin:
    base-url: http://localhost:9411
```

   * Screenshots of Zipkin UI in our app :
    

## 11. Notification with RabbitMQ 

### 1. Why a message broker

   * Message broker is a service that allows you to send and receive messages or notifications. <br/>
   * The purpose is to make an asynchronous communication between microservices.
   * In case we have an outage of notification microservice, we can send requests asynchronously to the message broker, <br> until the notification microservice is up again.
   * Another use case, is about creating many instances of notification microservice to resolve the charges. <br> **_But_** in this case we don't know how many instances we need, So will have `**_scaling issues_**`.

> Solution :
> * The solution in this case, is about adding a message broker to the application, so in case of an outage of the notification microservice, we can send requests asynchronously to the message broker, <br> until the notification microservice is up again.

### 2. AMQP 

   * Stand-for : **Advanced Message Queuing Protocol** <br> Is a messaging protocol that allows clients apps to communicate with each other using a messaging middleware brokers (RabbitMQ, Kafka, etc.).

   * Product / Customer & Orders microservices are producers of messages in our app.
   * Notification microservice is a consumer of messages in our app.
   * Our queue is the RabbitMQ broker.
   * Illustration :
<p align="center">
    <img src="docs/images/rabbitMQ.png" alt="RabbitMQ">
</p>

> Note : We can create multiple queues in RabbitMQ.

### 3. Setup RabbitMQ 

   * Add new module called "ampq", which contains the RabbitMQ configuration and RabbitMQ producer.
   * **`RabbitMQConfig`** :
     * Setup exchange, queue and the routing key.
     * Bind the queue to the exchange, in order to send messages to a specific queue.
   * **`RabbitMQProducer`** :
     * Contain the publish method to send messages to the queue.

   * Add maven dependency for RabbitMQ in **`pom.xml`** :
    
```yaml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

   * Consume messages from the queue in notification microservice, and store infos in the BD (Example in customer microservice).
    
```java
public class NotificationConsumer {
    @RabbitListener(queues = "${rabbitmq.queue.notification}")
    public void consumer(CustomerNotificationRequest customerNotificationRequest) {
        log.info("Consumed {} from queue", customerNotificationRequest);
        notificationService.sendNotification(customerNotificationRequest);
    }
}
```

## 12. Feign clients module 

  * Add separate module for Feign clients.
  * When we would call an API from another microservice, we will add feign clients to the microservice as dependency.
  * For example, in customer microservice, we will add feign clients to pom.xml in order to use product and order APIs.

## 13. Containerizing microservices using JIB maven plugin 

### 1. What is JIB 

   * `JIB` is a maven plugin that allows you to create a Docker image for your app.
   * Go check my blog [post](https://www.miliari.me/blog/dockerizing-spring-boot-app-with-jib) about Jib to see more details about google plugin.

### 2. Setup JIB 

   * Add JIB dependency in **`pom.xml`** of the whole microservice using `profiles` tag :
    
```yaml
<profiles>
    <profile>
        <id>jib-build-push-docker-image</id>
        <activation>
            <activeByDefault>false</activeByDefault>
        </activation>
        <build>
            <plugins>
                <plugin>
                    <groupId>com.google.cloud.tools</groupId>
                    <artifactId>jib-maven-plugin</artifactId>
                </plugin>
            </plugins>
        </build>
    </profile>
</profiles>
```

   * We should also configure `**docker-jib**` in the parent module by specifying java image and its version, as well as the goal of the build (check pom.xml of the parent module).
   * Create a new docker config file for the application to be matched with the images of each microservice.
   * Update docker-compose.yml file by adding the images, ports and the relationships between them.
   * Build the new images using the following command :
   > docker login # (if you are not logged in)
   * **_`!! Note !!`_** : Concerning the packaging of microservices to JARs, first we need to install from the root using maven to be able to use modules as dependencies in other project.
   > mvn clean package -P build-docker-image
   * Run the following command : 
   > docker-compose up -d
   * Create databases via PGAdmin

## 14. Deploy microservices to local Kubernetes 

### 1. What is Kubernetes 

   * Kubernetes is a container orchestration tool, that allows you to create a cluster of containers, and deploy them locally or on different cloud providers.
   * K8S manage and deploy applications.
   * Give us the possibility to scale up and down according to the needs of our application.
   * The K8S cluster is divided in two nodes : **_master node_** and **_worker nodes_**.
   * **`The master node`** is the node that is responsible for managing the cluster (brain of the cluster), where all the decisions are made.
   * **`The worker nodes`** are the nodes that are responsible for running the applications.

<p align="center">
    <img src="docs/images/k8s-cluster.png" alt="k8s-cluster">
</p>

- To deploy our microservices to Kubernetes, we need to create deployment and service for each microservice.

### 2. What is Deployment

   * K8 deployment is a manifest to facilitate the deployment of the app in pods (in k8s we talk about pods not containers).
   * Deployment allows to create replicaSets (instances of the pod) and configure them, versus POD that give us to create just one instance.

### 3. What are Services in k8s

   * Service manifest in k8s define how we can access the application.

<p align="center">
    <img src="docs/images/k8s-services.png" alt="k8s-services">
</p>

   * For testing purposes, we can use port forwarding to access the application.
   * There's different types of services :
       * ClusterIP
       * LoadBalancer
       * NodePort
       * ExternalName

### 4. Setup Kubernetes 

* Check k8S directory -> minikube -> services.

> **_Note_**: We should never deploy our DB on K8S, because in case that node can die then you will lose the hole data of the application.
> Instead of that, we can use some services like RDS via AWS.

### 5. Deploy Postgres RabbitMQ & Zipkin to K8S

   * Deploy Postgres to K8S :
      * Create `config-map` that contains the credentials of the database.
      * Create `service` manifest in order to define how we can access the database (ClusterIP).
      * Create a `volume` using "PersistentVolume" and "PersistentVolumeClaim".
      * For postgres we will use `statefulset` instead of deployment because the **_stateful-set support long-lived app such as databases_** (Save data in case that pod restart).
  
   * Run the following commands :
   >minikube start 

   >cd k8s/miniKube

   >kubectl apply -f bootstrap/postgres
  
   * SSH postgres in order to create DBs (all that stuff just for testing purposes locally) :
   
   >get pods (retrieve "postgres" pod name)

   >kubectl exec -it postgres-0 -- psql -U miliariadnane

   >CREATE DATABASE customer; CREATE DATABASE order; CREATE DATABASE product; CREATE DATABASE notification;

   * Same thing for RabbitMQ and Zipkin after creating the manifests : 

   >kubectl apply -f bootstrap/rabbitmq
  
   >kubectl apply -f bootstrap/zipkin

### 6. No need for Gateway anymore 

   * Kubernetes provides us load balancer that manage the requests to the microservices, <br> so the best scenario is to use managed LB instead of configure us using cloud providers like AWS, GCP, etc.

### 7. No need for Service Discovery also 

   * Kubernetes provides us a service discovery mechanism for free without any configuration.
   * Some resources here explain how k8s give us Service Discovery dynamically :
     * [link 1](https://www.densify.com/kubernetes-autoscaling/kubernetes-service-discovery#:~:text=One%20way%20Kubernetes%20provides%20service,of%20pods%20in%20an%20application.)
     * [link 2](https://medium.com/avmconsulting-blog/service-discovery-for-microservices-with-kubernetes-bf4353953553)

### 8. Ingress

   * Kubernetes Ingress is an API object that provides routing rules to manage external users access to the services in a Kubernetes cluster.
   * Ingress can provide load balancing, SSL termination and name-based virtual hosting.
   * We must add ingress in our kubernetes minikube cluster to access our services from outside the cluster by specifying the customized path and host name for each microservice, also to manage load balancing.

   To read more about ingress, you can check k8s documentation : [Ingress k8s doc](https://kubernetes.io/docs/concepts/services-networking/ingress/)

<p align="center">
    <img src="docs/images/ingress.jpg" alt="k8s ingress">
</p>

## 15. Configure AWS EKS cluster 

### 1. What is EKS 

### 2. Setup EKS (solution 1)
    
   1. Create a new cluster (master node) :
      * Create AWS account (free tier for testing purposes).
      * Create a VPC
      * Create an **IAM** (Identity and Access Management) role with Security Group.
        * When we say "`IAM role`", it means create AWS user.
        * And "`Security Group`" means a list of permissions that the user can access.
        
   2. Create cluster for node groups (worker nodes) :
      * choose a name for the cluster, k8s version, the region and the VPC.
      * Set security groups for the cluster.
  
   3. Create worker nodes & connect to cluster 
      * We will create worker nodes using "`EC2 instances`".
      * create Worker nodes as group of nodes.
      * choose cluster it will attach to.
      * Define security groups, instance type, resources.
      * Define max & min number of nodes (autoscaling).

### 3. Setup EKS (solution 2) : using **`eksctl`** check this [link](https://eksctl.io/docs/getting-started/)

### 4. Add eks application config file for each microservice

## 16. create databases on AWS RDS

* Check this [link](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/USER_ConnectToPostgreSQLInstance.html)

## 17. Monitoring microservices with Prometheus and Grafana

### 1. Why monitoring 

* `Monitoring` is a concept that allows us to monitor and supervise the performance of our application using metrics like Http traces, health checks, CPU, memory, etc.
* All of that information can be exposed and collected by Prometheus and Grafana using `micrometer`.

### 2. What is Micrometer 

* Micrometer is a library that provides a metric facade that collect metrics from JVM-based application and services then exposes this data with external application.

### 3. Setup monitoring configuration 

   1. Add "micrometer" and "prometheus" to the dependencies of microservices :
      
```yaml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

```yaml
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
    <version>1.9.2</version>
</dependency>
```
      
   2. Add endpoints configuration to the application.yml :
      
```yaml
management:
endpoints:
  web:
      exposure:
           include: "*"
endpoint:
   health:
      show-details: always
```

   3. Add docker images to docker compose.

   4. Monitoring diagram :

<p align="center">
    <img src="docs/diagrams/monitoring-diagram.png" alt="monitoring diagram">
</p>

## 18. deploy microservices to EKS cluster using git actions

### 1. Create deploy workflow 

   1. login to docker hub
   2. maven clean package & push custom image to docker hub
   3. update images version on deployments services manifests and commit changes 
   4. configure aws credentials to access EKS cluster
   5. update kube config
   6. deploy services to EKS cluster (apply manifests)

### 2. Changes application yaml files 
   * add db username & password as environment variables

### 3. Add database username & password to deployment.yaml file as secrets 

```yaml
env:
- name: SPRING_PROFILES_ACTIVE
   value: eks
- name: DB_USERNAME
   valueFrom:
      secretKeyRef:
        name: dbsecretd
        key: db_username
- name: DB_PASSWORD
   valueFrom:
      secretKeyRef:
        name: dbsecret
        key: db_password
```

### 4. Add secrets to eks cluster using command line locally 
   1. Install kubectl : [link](https://kubernetes.io/docs/tasks/tools/install-kubectl-windows/)
   2. Install aws cli : [link](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html)
   3. Configure and credential file settings in local machine : [link](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-files.html)

      ```
      [default]
      aws_access_key_id=AKIAIOSFODNN7EXAMPLE
      aws_secret_access_key=wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
      ```
   4. Install eksctl :
      * [link 1](https://docs.aws.amazon.com/eks/latest/userguide/eksctl.html)
      * [link 2](https://github.com/weaveworks/eksctl)

   5. Add IAM permissions : The IAM security principal that you're using must have permissions to work with Amazon EKS IAM roles and service linked roles, and a VPC and related resources.
   6. Verify kubectl configuration
      > kubectl cluster-info
   7. Update kube config (exist in ~/.kube/config)
      * Run the following command :
        > **exe** : aws eks update-kubeconfig --name [name-of-eks-cluster] --region [aws-region]
        
        > aws eks update-kubeconfig --name demo-microservices --region ca-central-1
      * [Link](https://docs.aws.amazon.com/cli/latest/reference/eks/update-kubeconfig.html) 
   8. Check nodes status :
      > kubectl get nodes
   9. Add secrets :
      > kubectl create secret generic dbsecret --from-literal=db_username=your-db-username --from-literal=db_password=your-db-password
      * db_username & db_password are environment variables in our eks-application.yml files.

## 19. Deployment workflow diagram

![deployment workflow](docs/diagrams/deploy-workflow-diagram.png)


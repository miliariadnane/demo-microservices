package dev.nano;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiKeyManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiKeyManagerApplication.class, args);
    }
}

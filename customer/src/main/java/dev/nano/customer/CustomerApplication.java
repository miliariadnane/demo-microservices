package dev.nano.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(
        scanBasePackages = {
                "dev.nano.customer",
                "dev.nano.amqp",
                "dev.nano.clients"
        }
)
@EnableFeignClients(
        basePackages = "dev.nano.clients"
)
@EnableDiscoveryClient
@ComponentScan(basePackages = "dev.nano")
public class CustomerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }
}

package dev.nano.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(
        scanBasePackages = {
                "dev.nano.order",
                "dev.nano.amqp"
        }
)
@EnableDiscoveryClient
@EnableFeignClients(
        basePackages = "dev.nano.clients"
)
@ComponentScan(basePackages = "dev.nano")
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}

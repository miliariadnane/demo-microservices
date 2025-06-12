package dev.nano.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
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
@PropertySources({
        @PropertySource("classpath:amqp-${spring.profiles.active}.properties"),
        @PropertySource("classpath:clients-${spring.profiles.active}.properties")
})
@ComponentScan(basePackages = "dev.nano")
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}

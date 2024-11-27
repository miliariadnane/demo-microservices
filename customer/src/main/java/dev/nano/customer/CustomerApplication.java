package dev.nano.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication(
        scanBasePackages = {
                "dev.nano.customer",
                "dev.nano.amqp"
        }
)
@EnableFeignClients(
        basePackages = "dev.nano.clients"
)
@EnableDiscoveryClient
@PropertySources({
        @PropertySource("classpath:amqp-${spring.profiles.active}.properties"),
        @PropertySource("classpath:clients-${spring.profiles.active}.properties")
})
public class CustomerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }
}

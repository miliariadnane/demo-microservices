package dev.nano.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication(
        scanBasePackages = {
                "dev.nano.order",
                "dev.nano.amqp"
        }
)
@EnableEurekaClient
@EnableFeignClients(
        basePackages = "dev.nano.clients"
)
@PropertySources({
        @PropertySource("classpath:amqp-${spring.profiles.active}.properties"),
        @PropertySource("classpath:clients-${spring.profiles.active}.properties")
})
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}

package dev.nano.customer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerDbSeeder {

    @Bean
    CommandLineRunner commandLineRunner(CustomerRepository customerRepository) {
        return args -> {
            CustomerEntity customer = CustomerEntity.builder()
                    .name("Adnane Miliari")
                    .email("miliari.adnane@gmail.com")
                    .phone("+61112223333")
                    .address("Rabat, Morocco")
                    .build();
            customerRepository.save(customer);
        };
    }
}

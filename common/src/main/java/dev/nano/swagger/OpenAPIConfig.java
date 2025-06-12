package dev.nano.swagger;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(OpenAPIProperties.class)
public class OpenAPIConfig {

    private final OpenAPIProperties properties;

    public OpenAPIConfig(OpenAPIProperties properties) {
        this.properties = properties;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(properties.getTitle())
                        .version(properties.getVersion())
                        .description(properties.getDescription())
                        .contact(new Contact()
                                .name(properties.getContact().getName())
                                .email(properties.getContact().getEmail())
                                .url(properties.getContact().getUrl()))
                        .license(new License()
                                .name(properties.getLicense().getName())
                                .url(properties.getLicense().getUrl())))
                .externalDocs(new ExternalDocumentation()
                        .description(properties.getExternalDocs().getDescription())
                        .url(properties.getExternalDocs().getUrl()));
    }
}

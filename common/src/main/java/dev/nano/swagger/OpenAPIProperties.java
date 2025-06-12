package dev.nano.swagger;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "openapi")
@Getter @Setter
public class OpenAPIProperties {
    private String title;
    private String version;
    private String description;
    private Contact contact = new Contact();
    private License license = new License();
    private ExternalDocs externalDocs = new ExternalDocs();

    @Getter
    @Setter
    public static class Contact {
        private String name;
        private String email;
        private String url;
    }

    @Getter
    @Setter
    public static class License {
        private String name;
        private String url;
    }

    @Getter
    @Setter
    public static class ExternalDocs {
        private String description;
        private String url;
    }
}

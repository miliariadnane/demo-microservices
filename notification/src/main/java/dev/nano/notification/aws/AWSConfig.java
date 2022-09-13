package dev.nano.notification.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class AWSConfig {
    private Environment environment;

    public AWSConfig(Environment environment) {
        this.environment = environment;
    }

    private static final String ACCESS_KEY_BUCKET = "ACCESS_KEY_BUCKET";
    private static final String SECRET_KEY_BUCKET = "ACCESS_KEY_BUCKET";

    @Bean
    public AmazonSimpleEmailService emailService() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(
            environment.getRequiredProperty(ACCESS_KEY_BUCKET),
            environment.getRequiredProperty(SECRET_KEY_BUCKET)
        );
        return AmazonSimpleEmailServiceClientBuilder
                .standard()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}

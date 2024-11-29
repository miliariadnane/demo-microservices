package dev.nano.apikey;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDKeyGeneratorImpl implements KeyGenerator {
    @Override
    public String generateKey() {
        return UUID.randomUUID().toString();
    }
}

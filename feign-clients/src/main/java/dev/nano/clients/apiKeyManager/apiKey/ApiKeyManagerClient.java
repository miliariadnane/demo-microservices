package dev.nano.clients.apiKeyManager.apiKey;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "apiKey-manager", url = "${clients.apiKey-manager.url}")
public interface ApiKeyManagerClient {

    @GetMapping("{apiKey}/applications/{applicationName}/authorization")
    boolean isKeyAuthorizedForApplication(
            @PathVariable("apiKey") String apiKey,
            @PathVariable("applicationName") String applicationName);

    @PutMapping("/api/v1/apiKey-manager/api-keys/{apiKey}/revoke")
    void revokeKey(@PathVariable("apiKey") String apiKey);
}

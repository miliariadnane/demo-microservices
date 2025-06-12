package dev.nano.controller;

import dev.nano.apikey.ApiKeyConstant;
import dev.nano.apikey.ApiKeyRequest;
import dev.nano.apikey.ApiKeyService;
import dev.nano.application.ApplicationName;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dev.nano.swagger.BaseController;

@RestController
@RequestMapping(path = ApiKeyConstant.API_KEY_URI_REST_API)
@Tag(name = BaseController.API_KEY_TAG, description = BaseController.API_KEY_DESCRIPTION)
@AllArgsConstructor
public class ApiKeyController {
    private final ApiKeyService apiKeyService;

    @Operation(
            summary = "Generate new API key",
            description = "Create a new API key with specified client details and permissions"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "API key generated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "string"))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<String> generateNewApiKey(
            @RequestBody ApiKeyRequest apiKeyRequest) {
        return ResponseEntity.ok(apiKeyService.save(apiKeyRequest));
    }

    @Operation(
            summary = "Revoke API key",
            description = "Disable and revoke an existing API key"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API key revoked successfully"),
            @ApiResponse(responseCode = "404", description = "API key not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("{apiKey}/revoke")
    public void revokeKey(@PathVariable("apiKey") String apiKey) {
        apiKeyService.revokeApi(apiKey);
    }

    @Operation(
            summary = "Check API key authorization",
            description = "Verify if an API key is authorized for a specific application"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Authorization check completed",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "boolean"))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("{apiKey}/applications/{applicationName}/authorization")
    public ResponseEntity<Boolean> isKeyAuthorizedForApplication(
            @PathVariable("apiKey") String apiKey,
            @PathVariable("applicationName") ApplicationName applicationName) {
        return ResponseEntity.ok(apiKeyService.isAuthorized(apiKey, applicationName));
    }
}

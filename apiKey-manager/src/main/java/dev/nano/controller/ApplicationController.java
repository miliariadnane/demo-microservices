package dev.nano.controller;

import dev.nano.application.ApplicationConstant;
import dev.nano.application.ApplicationName;
import dev.nano.application.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dev.nano.swagger.BaseController;

@RestController
@RequestMapping(path = ApplicationConstant.APPLICATION_URI_REST_API)
@Tag(name = BaseController.APPLICATION_TAG, description = BaseController.APPLICATION_DESCRIPTION)
@AllArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    @Operation(
            summary = "Revoke application access",
            description = "Remove application access from a specific API key"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application access revoked successfully"),
            @ApiResponse(responseCode = "404", description = "Application or API key not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("{applicationName}/revoke/{apiKey}")
    public void revokeApplicationFromApiKey(
            @PathVariable("applicationName") String applicationName,
            @PathVariable("apiKey") String apiKey) {
        applicationService.revokeApplicationFromApiKey(applicationName, apiKey);
    }

    @Operation(
            summary = "Assign application to API key",
            description = "Grant application access to a specific API key"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application assigned successfully"),
            @ApiResponse(responseCode = "404", description = "API key not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("{applicationName}/revoke/{apiKey}")
    public void assignApplicationToApiKey(
            @PathVariable("applicationName") ApplicationName applicationName,
            @PathVariable("apiKey") String apiKey) {
        applicationService.assignApplicationToApiKey(applicationName, apiKey);
    }
}

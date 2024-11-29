package dev.nano.application;

public interface ApplicationService {
    void assignApplicationToApiKey(ApplicationName applicationName, String apiKey);
    void revokeApplicationFromApiKey(String applicationName, String apiKey);
}

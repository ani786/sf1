package com.mysf.sfwebclient.api.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class SalesforceConfig {
    @Value("${salesforce.auth-endpoint}")
    private String authEndpoint;

    @Value("${salesforce.username}")
    private String username;

    @Value("${salesforce.password}")
    private String password;

    @Value("${salesforce.client-id}")
    private String clientId;

    @Value("${salesforce.client-secret}")
    private String clientSecret;

    @Value("${salesforce.security-token}")
    private String securityToken;

    @Value("${salesforce.api-version}")
    private String apiVersion;

    @Bean
    public WebClient salesforceWebClient() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("username", username);
        formData.add("password", password + securityToken);

        WebClient webClient = WebClient.builder()
                .baseUrl(authEndpoint)
                .build();

        AccessTokenResponse accessTokenResponse = webClient.post()
                .uri("/services/oauth2/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(AccessTokenResponse.class)
                .block();

        String accessToken = accessTokenResponse.getAccessToken();

        return WebClient.builder()
                .baseUrl("https://yourinstance.salesforce.com/services/data/v" + apiVersion)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .build();
    }

    private static class AccessTokenResponse {
        @JsonProperty("access_token")
        private String accessToken;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }
    }
}


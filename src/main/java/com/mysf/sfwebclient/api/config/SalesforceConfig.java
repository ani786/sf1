package com.mysf.sfwebclient.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.WebSessionServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SalesforceConfig {

    @Value("${spring.security.oauth2.client.registration.salesforce.username}")
    private String username;

    @Value("${spring.security.oauth2.client.registration.salesforce.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.salesforce.client-secret}")
    private String clientSecret;

    List<String> scope = Arrays.asList("api", "refresh_token", "offline_access");


    @Value("${spring.security.oauth2.client.registration.salesforce.api-version}")
    private String apiVersion;

    @Value("${spring.security.oauth2.client.registration.salesforce.instance-url}")
    private String instanceUrl;

    @Value("${spring.security.oauth2.client.registration.salesforce.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.provider.salesforce.authorization-uri}")
    private String authorizeUri;

    @Value("${spring.security.oauth2.client.provider.salesforce.token-uri}")
    private String tokenUri;



    @Bean
    public ClientRegistration salesforceClientRegistration() {
        return ClientRegistration
                .withRegistrationId("salesforce")
                .clientId(clientId)
                .clientSecret(clientSecret)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .tokenUri(tokenUri)
                .scope(scope)
                .redirectUri(redirectUri)
                .authorizationUri(authorizeUri)
                .build();
    }

    @Bean
    public ReactiveClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryReactiveClientRegistrationRepository(salesforceClientRegistration());
    }

    @Bean
    public ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {

        ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider = ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .build();

        DefaultReactiveOAuth2AuthorizedClientManager authorizedClientManager =
                new DefaultReactiveOAuth2AuthorizedClientManager(
                        clientRegistrationRepository, authorizedClientRepository);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }

    @Bean
    public ServerOAuth2AuthorizedClientRepository authorizedClientRepository() {
        return new WebSessionServerOAuth2AuthorizedClientRepository();
    }

    @Bean
    public ExchangeStrategies exchangeStrategies() {
        return ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024))
                .build();
    }

    @Bean
    public WebClient salesforceWebClient(
            ReactiveOAuth2AuthorizedClientManager authorizedClientManager,
            ExchangeStrategies exchangeStrategies) {

        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
                new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);

        oauth2.setDefaultClientRegistrationId("salesforce");

        return WebClient.builder()
                .baseUrl(instanceUrl + "/services/data/v" + apiVersion)
                .filter(oauth2)
                .exchangeStrategies(exchangeStrategies)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + authorizedClientManager.authorize(
                        OAuth2AuthorizeRequest.withClientRegistrationId("salesforce")
                                .principal(username)
                                .build())
                        .flatMap(client -> Mono.just(client.getAccessToken().getTokenValue())))
                .build();
    }

}


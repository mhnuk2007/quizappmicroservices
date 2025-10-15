package com.learning.apigateway.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
public class ConfigurationFilter extends AbstractGatewayFilterFactory<ConfigurationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public ConfigurationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {

                if (!exchange.getRequest().getHeaders().containsKey("Authorization")) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing Authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                } else {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Authorization header format");
                }

                // Reactive non-blocking call using WebClient
                return webClientBuilder.build()
                        .get()
                        .uri("lb://AUTH-SERVICE/auth/validate?token=" + authHeader)
                        .retrieve()
                        .onStatus(status -> status.isError(),
                                response -> response.createException().flatMap(error ->
                                        Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token"))))
                        // Convert ResponseSpec → Mono
                        .bodyToMono(String.class)
                        // Continue the filter chain after successful validation
                        .flatMap(response -> chain.filter(exchange))
                        // Handle unexpected errors gracefully
                        .onErrorResume(e -> Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access")));
            }

            return chain.filter(exchange);
        };
    }

    public static class Config { }
}

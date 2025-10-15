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
            if (!validator.isSecured.test(exchange.getRequest())) {
                // Open route, no auth required
                return chain.filter(exchange);
            }

            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header"));
            }

            String token = authHeader.substring(7);

            // Reactive call to Auth-Service
            return webClientBuilder.build()
                    .get()
                    .uri("http://AUTH-SERVICE/auth/validate?token=" + token)
                    .retrieve()
                    .onStatus(status -> status.isError(), clientResponse ->
                            Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token")))
                    .bodyToMono(String.class)
                    .flatMap(response -> chain.filter(exchange));

        };
    }

    public static class Config { }
}

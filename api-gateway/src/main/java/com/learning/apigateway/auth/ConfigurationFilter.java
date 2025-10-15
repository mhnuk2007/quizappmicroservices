package com.learning.apigateway.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ConfigurationFilter extends AbstractGatewayFilterFactory<ConfigurationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private RestTemplate template;

    public ConfigurationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {

                // ✅ Check for Authorization header
                if (!exchange.getRequest().getHeaders().containsKey("Authorization")) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing Authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

                // ✅ Extract token
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                } else {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Authorization header format");
                }

                try {
                    // ✅ Call Auth-Service to validate token
                    template.getForObject("http://localhost:8083/auth/validate?token=" + authHeader, String.class);
                } catch (Exception e) {
                    System.out.println("Invalid access: " + e.getMessage());
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access to application");
                }
            }

            // ✅ Continue to next filter in chain
            return chain.filter(exchange);
        };
    }

    public static class Config {
        // Currently empty, but you can add config fields later if needed
    }
}

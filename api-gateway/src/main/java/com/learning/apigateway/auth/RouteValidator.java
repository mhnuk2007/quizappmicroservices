package com.learning.apigateway.auth;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    // Public routes that don't require authentication
    public static final List<String> openApiEndpoints = List.of(
            "/auth/signup",
            "/auth/signin",
            "/auth/validate",
            "/eureka"
    );

    // Predicate to check if a route is secured
    public Predicate<ServerHttpRequest> isSecured = request -> {
        String path = request.getURI().getPath();

        // Check if the path exactly matches or starts with any open endpoint
        return openApiEndpoints.stream()
                .noneMatch(uri -> path.equals(uri) || path.startsWith(uri + "/"));
    };
}
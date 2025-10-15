package com.learning.apigateway.auth;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    // Public routes that don’t require authentication
    public static final List<String> openApiEndpoints = List.of(
            "/auth/signup",
            "/auth/signin",
            "/eureka"
    );

    // Predicate to check if a route is secured
    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}

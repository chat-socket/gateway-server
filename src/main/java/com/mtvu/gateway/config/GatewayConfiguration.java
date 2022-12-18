package com.mtvu.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfiguration {

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(p -> p
                .path("/websocket/**")
                .filters(f -> f.rewritePath("/websocket/(?<segment>.*)","/${segment}"))
                .uri("http://websocket-server:80"))
            .route(p -> p
                .path("/auth/**")
                .filters(f -> f.rewritePath("/auth/(?<segment>.*)","/${segment}"))
                .uri("http://identity-authorization-server:80"))

            .build();
    }
}

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
                .path("/websocket", "/websocket/**")
                    .filters(gatewayFilterSpec -> gatewayFilterSpec
                            .dedupeResponseHeader("Access-Control-Allow-Origin","RETAIN_UNIQUE")
                            .dedupeResponseHeader("Access-Control-Allow-Credentials","RETAIN_UNIQUE")
                    )
                .uri("http://websocket-server:80"))

            .build();
    }
}

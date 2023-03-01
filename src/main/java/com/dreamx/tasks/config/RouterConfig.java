package com.dreamx.tasks.config;

import com.dreamx.tasks.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfig {

    private final ServiceConfig serviceConfig;

    public RouterConfig(ServiceConfig serviceConfig) {
        this.serviceConfig = serviceConfig;
    }

    @Bean
    RouterFunction<ServerResponse> routes(UserHandler handler) {
        return route(POST("api/v1/users"), handler::create)
                .andRoute(GET("api/v1/user/{userId}"), handler::getUserById)
                .andRoute(GET("api/v1/users"), handler::findAll)
                .andRoute(PUT("api/v1/users/{userId}").and(contentType(MediaType.APPLICATION_JSON)), handler::update)
                .andRoute(GET("/status"), request -> ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).bodyValue("OK"));

        // .andRoute(DELETE("/handler/users/{userId}").and(accept(MediaType.APPLICATION_JSON)), handler::deleteUserById);
    }

    @Bean
    UserHandler handler() {
        return new UserHandler(serviceConfig.userService());
    }
}

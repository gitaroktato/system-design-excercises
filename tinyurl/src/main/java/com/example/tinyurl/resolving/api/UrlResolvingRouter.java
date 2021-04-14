package com.example.tinyurl.resolving.api;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration(proxyBeanMethods = false)
public class UrlResolvingRouter {

    @Bean
    public RouterFunction<ServerResponse> urlResolve(UrlResolvingHandler handler) {
        return route(GET("/{hash}"), handler::resolve);
    }

}

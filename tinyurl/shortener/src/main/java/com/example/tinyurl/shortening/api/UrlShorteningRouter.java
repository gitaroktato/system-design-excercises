package com.example.tinyurl.shortening.api;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration(proxyBeanMethods = false)
public class UrlShorteningRouter {

    @Bean
    public RouterFunction<ServerResponse> urlShorten(UrlShorteningHandler handler) {
        return route(POST("/v1/url").and(accept(APPLICATION_JSON)), handler::shorten);
    }

}

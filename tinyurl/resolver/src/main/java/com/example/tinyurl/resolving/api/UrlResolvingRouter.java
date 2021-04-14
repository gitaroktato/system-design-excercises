package com.example.tinyurl.resolving.api;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration(proxyBeanMethods = false)
public class UrlResolvingRouter {

    @Bean
    public RouterFunction<ServerResponse> urlResolve(UrlResolvingHandler handler) {
        return route().GET("/{hash}", handler::resolve, ops -> ops
                .operationId("resolve")
                .summary("Resolve URL by the given shortened hash")
                .parameter(parameterBuilder().in(ParameterIn.PATH).name("hash").description("Shortened URL hash"))
                .response(responseBuilder().responseCode("308").description("Redirection to the resolved URL"))
                .response(responseBuilder().responseCode("400").description("Malformed URL during resolution"))
        ).build();
    }

}

package com.example.tinyurl.shortening.infrastructure.webflux;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;

import com.example.tinyurl.shortening.application.dto.UrlDto;
import com.example.tinyurl.shortening.application.dto.UrlShortenedDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration(proxyBeanMethods = false)
public class UrlShorteningRouter {

    @Bean
    public RouterFunction<ServerResponse> urlShorten(UrlShorteningHandler handler) {
        return route().POST("/v1/url",
                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                handler::shorten, ops -> ops.operationId("shorten")
                        .summary("Create a new shortened URL")
                        .requestBody(requestBodyBuilder().implementation(UrlDto.class))
                        .response(responseBuilder().responseCode("200").description("New shortened URL").implementation(UrlShortenedDto.class))
                        .response(responseBuilder().responseCode("400").description("Malformed URL"))
                        .build()
        ).build();
    }

}

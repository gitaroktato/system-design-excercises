package com.example.tinyurl.resolving.api;

import java.net.URISyntaxException;

import com.example.tinyurl.resolving.service.UrlResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class UrlResolvingHandler {

    @Autowired
    private UrlResolver resolver;

    public Mono<ServerResponse> resolve(ServerRequest request)  {
        var hashed = request.pathVariable("hash");
        return Mono.just(resolver.getRedirection(hashed))
                .flatMap(location -> ServerResponse.permanentRedirect(location).build())
                .onErrorResume(URISyntaxException.class, ex -> ServerResponse.badRequest().build());
    }
}

package com.example.tinyurl.resolving.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.stereotype.Service;
import reactor.core.Exceptions;

@Service
public class UrlResolver {

    public URI getRedirection(String hashed) {
        try {
            return new URI("https://google.com/" + hashed);
        } catch (URISyntaxException e) {
            throw Exceptions.propagate(e);
        }
    }
}

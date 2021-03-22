package com.example.demo.shortening.domain;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.util.DigestUtils;

public class UrlShortener {

    public static final String URL_PREFIX = "http://localhost/";
    public static final int URL_SHORTENED_LENGTH = 6;

    public static URL shorten(URL target) throws MalformedURLException {
        String value = target.toString();
        var hash = DigestUtils.md5Digest(value.getBytes(StandardCharsets.UTF_8));
        var encoded = Base64.getEncoder().encodeToString(hash);
        encoded = encoded.substring(0, URL_SHORTENED_LENGTH);
        return new URL(URL_PREFIX + encoded);
    }
}

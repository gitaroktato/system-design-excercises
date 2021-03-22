package com.example.demo.shortening;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class UrlShortener {

    public static final String URL_PREFIX = "http://localhost/";
    public static final int URL_SHORTENED_LENGTH = 6;

    public static URL shorten(URL target) throws MalformedURLException {
        String value = target.toString();
        var bytes = value.getBytes(StandardCharsets.UTF_8);
        var encoded = Base64.getEncoder().encodeToString(bytes);
        encoded = encoded.substring(0, URL_SHORTENED_LENGTH);
        return new URL(URL_PREFIX + encoded);
    }
}

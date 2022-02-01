package com.example.tinyurl.shortening.domain;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public final class Url {

    private final String urlString;

    private Url(String urlString) {
        this.urlString = urlString;
    }

    public static Url from(String urlString) throws MalformedURLException {
        return from(new URL(urlString));
    }

    public static Url from(URL url) {
        return new Url(url.toString());
    }

    public static Url from(String host, String hashKey, Optional<Integer> port, Optional<String> path)
            throws MalformedURLException {
        var urlString = "http://" + host +
                port.map(p -> ":" + p).orElse("")
                + path.map(p -> "/" + Url.stripPathFromForwardSlashes(p)).orElse("")
                + "/" + hashKey;
        var url = new URL(urlString);
        return from(url);
    }

    public static Url from(String host, String hashKey) throws MalformedURLException {
        return from(host, hashKey, Optional.empty(), Optional.empty());
    }

    public static Url from(String host, String hashKey, String path) throws MalformedURLException {
        return from(host, hashKey, Optional.empty(), Optional.ofNullable(path));
    }

    public static Url from(String host, String hashKey, Integer port, String path) throws MalformedURLException {
        return from(host, hashKey, Optional.ofNullable(port), Optional.ofNullable(path));
    }

    private static String stripPathFromForwardSlashes(String path) {
        return path.replaceFirst("^/+", "")
                .replaceFirst("/+$", "");
    }

    @Override
    public String toString() {
        return urlString;
    }

    public String getHashKey() {
        int hashIndex = urlString.lastIndexOf('/') + 1;
        return urlString.substring(hashIndex);
    }

    public byte[] toBytes() {
        return urlString.getBytes(StandardCharsets.UTF_8);
    }
}

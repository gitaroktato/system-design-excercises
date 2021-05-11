package com.example.tinyurl.shortening.domain;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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

    public static Url from(String host, int port, String path, String hashKey) throws MalformedURLException {
        var pathStripped = stripPathFromForwardSlashes(path);
        var hashKeyReplaced = hashKey.replaceAll("/", "_");
        var urlString = String.format("http://%s:%s/%s/%s", host, port,
                pathStripped, hashKeyReplaced);
        return from(urlString);
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

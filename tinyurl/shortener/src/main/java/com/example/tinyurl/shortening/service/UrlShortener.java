package com.example.tinyurl.shortening.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UrlShortener {

    @Value("${server.address}")
    private String serverAddress;

    @Value("${server.port}")
    private String serverPort;

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public static final int URL_SHORTENED_LENGTH = 6;

    public URL shorten(URL target) throws MalformedURLException {
        String value = target.toString();
        var hash = DigestUtils.md5Digest(value.getBytes(StandardCharsets.UTF_8));
        var encoded = Base64.getEncoder().encodeToString(hash);
        encoded = encoded.substring(0, URL_SHORTENED_LENGTH);
        var resultUrl = String.format("http://%s:%s/%s", serverAddress, serverPort, encoded);
        return new URL(resultUrl);
    }
}

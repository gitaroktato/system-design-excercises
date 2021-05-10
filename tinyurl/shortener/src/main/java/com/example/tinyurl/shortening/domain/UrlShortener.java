package com.example.tinyurl.shortening.domain;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UrlShortener {

    @Value("${application.resolver.host}")
    private String serverAddress;

    @Value("${application.resolver.path}")
    private String serverPath;

    @Value("${application.resolver.port}")
    private String serverPort;

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }

    public static final int URL_SHORTENED_LENGTH = 6;

    public URL shorten(URL target) throws MalformedURLException {
        String value = target.toString();
        var hash = DigestUtils.md5Digest(value.getBytes(StandardCharsets.UTF_8));
        var encoded = Base64.getEncoder().encodeToString(hash);
        encoded = encoded.substring(0, URL_SHORTENED_LENGTH);
        var resultUrl = String.format("http://%s:%s/%s/%s", serverAddress, serverPort,
                serverPath, encoded);
        return new URL(resultUrl);
    }
}

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
    private int serverPort;

    @Value("${application.shortener.keyLength}")
    private int hashKeyLength;

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }

    public void setHashKeyLength(int hashLength) {
        this.hashKeyLength = hashLength;
    }

    public Url shorten(Url target) throws MalformedURLException {
        var md5Sum = DigestUtils.md5Digest(target.toBytes());
        var hashKey = Base64.getEncoder().encodeToString(md5Sum);
        hashKey = hashKey.substring(0, hashKeyLength);
        return Url.from(serverAddress, serverPort, serverPath, hashKey);
    }
}

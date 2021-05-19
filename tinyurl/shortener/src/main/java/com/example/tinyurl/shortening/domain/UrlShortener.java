package com.example.tinyurl.shortening.domain;

import java.net.MalformedURLException;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UrlShortener {

    @Value("${application.resolver.host}")
    private String serverAddress;

    @Value("${application.resolver.path:#{null}}")
    private Optional<String> serverPath;

    @Value("${application.resolver.port:#{null}}")
    private Optional<Integer> serverPort;

    @Value("${application.shortener.keyLength}")
    private int hashKeyLength;

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = Optional.of(serverPort);
    }

    public void setServerPath(String serverPath) {
        this.serverPath = Optional.of(serverPath);
    }

    public void setHashKeyLength(int hashLength) {
        this.hashKeyLength = hashLength;
    }

    public Url shorten(Url target) throws MalformedURLException {
        var md5Sum = DigestUtils.md5Digest(target.toBytes());
        var hashKey = Base64.getEncoder().encodeToString(md5Sum);
        var hashKeyStripped = hashKey.substring(0, hashKeyLength);
        return Url.from(serverAddress, hashKeyStripped, serverPort, serverPath);
    }
}

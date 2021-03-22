package com.example.demo.shortening.api.dto;

public class UrlShortened {
    private final String originalUrl;
    private final String shortenedUrl;

    public UrlShortened(String originalUrl, String shortenedUrl) {
        this.originalUrl = originalUrl;
        this.shortenedUrl = shortenedUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getShortenedUrl() {
        return shortenedUrl;
    }
}

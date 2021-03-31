package com.example.tinyurl.shortening.api.dto;

public class UrlShortenedDto {
    private final String originalUrl;
    private final String shortenedUrl;

    public UrlShortenedDto(String originalUrl, String shortenedUrl) {
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

package com.example.tinyurl.shortening.application.dto;

public class UrlDto {
    public String originalUrl;
    public String alias;
    public String userName;

    @Override
    public String toString() {
        return "UrlDto{" +
                "originalUrl='" + originalUrl + '\'' +
                ", alias='" + alias + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}

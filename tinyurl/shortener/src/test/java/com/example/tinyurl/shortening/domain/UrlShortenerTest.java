package com.example.tinyurl.shortening.domain;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UrlShortenerTest {

    public static final String BASE_PATH = "/v1/url";
    public static final int HASH_LENGTH = 7;
    private UrlShortener urlShortener;

    @BeforeEach
    public void setUp() {
        urlShortener = new UrlShortener();
        urlShortener.setServerAddress("localhost");
        urlShortener.setServerPort("8081");
        urlShortener.setServerPath(BASE_PATH);
    }

    @Test
    public void testResolve() throws MalformedURLException {
        String target = "https://google.com/xasd";
        var result = urlShortener.shorten(new URL(target));
        Assertions.assertEquals(BASE_PATH.length() + HASH_LENGTH + 1,
                result.getPath().length());
    }

    @Test
    public void testResolveWithDifferentEnding() throws MalformedURLException {
        String target = "https://google.com/xasd";
        String target2 = "https://google.com/ImANewEnding";
        var result = urlShortener.shorten(new URL(target));
        var result2 = urlShortener.shorten(new URL(target2));
        Assertions.assertNotEquals(result, result2);
    }

}
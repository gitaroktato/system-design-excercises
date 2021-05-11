package com.example.tinyurl.shortening.domain;

import java.net.MalformedURLException;

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
        urlShortener.setServerPort(8081);
        urlShortener.setServerPath(BASE_PATH);
        urlShortener.setHashKeyLength(HASH_LENGTH);
    }

    @Test
    public void testResolve() throws MalformedURLException {
        String target = "https://google.com/xasd";
        var result = urlShortener.shorten(Url.from(target));
        Assertions.assertEquals(HASH_LENGTH, result.getHashKey().length());
    }

    @Test
    public void testResolveWithDifferentEnding() throws MalformedURLException {
        String target = "https://google.com/xasd";
        String target2 = "https://google.com/ImANewEnding";
        var result = urlShortener.shorten(Url.from(target));
        var result2 = urlShortener.shorten(Url.from(target2));
        Assertions.assertNotEquals(result, result2);
    }

    @Test
    public void testShorten_withReplacingForwardSlash() throws Exception {
        var url = "http://my-url/2657";
        var result = urlShortener.shorten(Url.from(url));
        Assertions.assertFalse(result.getHashKey().contains("/"),
                "Shortened URL should not contain forward slash: " + result);
    }
}
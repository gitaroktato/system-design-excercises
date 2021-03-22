package com.example.demo.shortening.domain;

import java.net.MalformedURLException;
import java.net.URL;

import com.example.demo.shortening.domain.UrlShortener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UrlShortenerTest {

    private UrlShortener urlShortener;

    @BeforeEach
    public void setUp() {
        urlShortener = new UrlShortener();
        urlShortener.setServerAddress("localhost");
        urlShortener.setServerPort("8080");
    }

    @Test
    public void testResolve() throws MalformedURLException {
        String target = "https://google.com/xasd";
        var result = urlShortener.shorten(new URL(target));
        Assertions.assertEquals(7, result.getPath().length());
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
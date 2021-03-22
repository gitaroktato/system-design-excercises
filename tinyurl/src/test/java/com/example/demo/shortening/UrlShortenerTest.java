package com.example.demo.shortening;

import java.net.MalformedURLException;
import java.net.URL;

import com.example.demo.shortening.domain.UrlShortener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UrlShortenerTest {

    @Test
    public void testResolve() throws MalformedURLException {
        String target = "https://google.com/xasd";
        var result = UrlShortener.shorten(new URL(target));
        Assertions.assertEquals(7, result.getPath().length());
    }

    @Test
    public void testResolveWithDifferentEnding() throws MalformedURLException {
        String target = "https://google.com/xasd";
        String target2 = "https://google.com/ImANewEnding";
        var result = UrlShortener.shorten(new URL(target));
        var result2 = UrlShortener.shorten(new URL(target2));
        Assertions.assertNotEquals(result, result2);
    }

}
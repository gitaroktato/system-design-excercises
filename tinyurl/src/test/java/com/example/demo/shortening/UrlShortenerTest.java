package com.example.demo.shortening;

import static org.junit.jupiter.api.Assertions.*;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.Test;

class UrlShortenerTest {

    @Test
    public void testResolve() throws MalformedURLException {
        String target = "https://google.com/xasd";
        var shortener = new UrlShortener();
        var result = shortener.shorten(new URL(target));
        System.out.println(result);
    }

}
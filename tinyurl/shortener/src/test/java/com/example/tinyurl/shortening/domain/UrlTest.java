package com.example.tinyurl.shortening.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.MalformedURLException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UrlTest {

    @Test
    public void testCreate() throws Exception {
        var fromString = "https://google.com/";
        var url = Url.from("https://google.com/");
        assertEquals(fromString, url.toString());
    }

    @Test
    public void testCreate_withInvalidUrl() {
        assertThrows(MalformedURLException.class, () -> Url.from("no one cares"));
    }

    @Test
    public void testGetHashKey() throws Exception {
        var target = Url.from("http://shorterner-api:8080/v1/url/2657");
        var result = target.getHashKey();
        Assertions.assertEquals("2657", result);
    }

    @Test
    public void testGetHashKey_withAllCharactersAllowed() throws Exception {
        var url = Url.from("http://shorterner-api:8080/v1/url/2abcdefnDpLghi_jQsklxw6+57");
        var result = url.getHashKey();
        Assertions.assertEquals("2abcdefnDpLghi_jQsklxw6+57", result);
    }

    @Test
    public void testCreate_withProperties() throws MalformedURLException {
        var url = Url.from("127.0.0.1", "xAKf4FDbuXd", 9090, "/v1/url");
        assertEquals("http://127.0.0.1:9090/v1/url/xAKf4FDbuXd", url.toString());
    }

    @Test
    public void testCreate_withoutAnyPort() throws MalformedURLException {
        var url = Url.from("127.0.0.1", "xAKf4FDbuXd", "/v1/url");
        assertEquals("http://127.0.0.1/v1/url/xAKf4FDbuXd", url.toString());
    }

    @Test
    public void testCreate_withoutAnyPath() throws MalformedURLException {
        var url = Url.from("127.0.0.1", "xAKf4FDbuXd");
        assertEquals("http://127.0.0.1/xAKf4FDbuXd", url.toString());
    }
}

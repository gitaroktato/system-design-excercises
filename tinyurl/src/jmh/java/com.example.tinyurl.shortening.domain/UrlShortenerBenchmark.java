package com.example.tinyurl.shortening.domain;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.openjdk.jmh.annotations.Benchmark;

public class UrlShortenerBenchmark {

    @Benchmark
    public URL benchmarkShorten() throws MalformedURLException {
        var shortener = new UrlShortener();
        var target = "https://google.com/";
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        var generatedString = new String(array, StandardCharsets.UTF_8);
        return shortener.shorten(new URL(target + generatedString));
    }
}

package service;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import com.example.tinyurl.shortening.service.UrlShortener;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class UrlShortenerBenchmark {

    @Benchmark
    public URL benchmarkShorten() throws MalformedURLException {
        var shortener = new UrlShortener();
        shortener.setServerAddress("localhost");
        shortener.setServerPort("8080");
        var target = "https://google.com/";
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        var generatedString = new String(array, StandardCharsets.UTF_8);
        return shortener.shorten(new URL(target + generatedString));
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(UrlShortenerBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}

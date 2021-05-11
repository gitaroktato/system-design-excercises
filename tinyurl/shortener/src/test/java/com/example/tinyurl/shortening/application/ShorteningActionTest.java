package com.example.tinyurl.shortening.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.tinyurl.shortening.application.dto.UrlDto;
import com.example.tinyurl.shortening.domain.Url;
import com.example.tinyurl.shortening.domain.UrlShortener;
import com.example.tinyurl.shortening.infrastructure.riak.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ShorteningActionTest {

    @Mock
    private UrlShortener shortener;
    @Mock
    private UrlRepository repository;
    private ShorteningAction target;

    @BeforeEach
    public void setUp() {
        target = new ShorteningAction();
        target.setShortener(shortener);
        target.setRepository(repository);
    }

    @Test
    public void testShorteningAction() throws Exception {
        var expectedShortening = "https://google.com/aXeBd/Ut";
        when(shortener.shorten(any(Url.class)))
                .thenReturn(Url.from(expectedShortening));
        var dto = new UrlDto();
        dto.originalUrl = "http://google.com";
        var result = target.doShortening(dto);
        assertEquals(dto.originalUrl, result.getOriginalUrl());
        assertEquals(expectedShortening, result.getShortenedUrl());
    }
}

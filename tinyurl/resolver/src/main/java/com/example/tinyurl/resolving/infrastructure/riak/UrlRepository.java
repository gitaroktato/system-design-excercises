package com.example.tinyurl.resolving.infrastructure.riak;

import static com.example.tinyurl.resolving.infrastructure.spring.CachingConfiguration.URL_CACHE_NAME;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;

import com.basho.riak.client.api.RiakClient;
import com.basho.riak.client.api.cap.Quorum;
import com.basho.riak.client.api.commands.kv.FetchValue;
import com.basho.riak.client.api.commands.kv.StoreValue;
import com.basho.riak.client.core.query.Location;
import com.basho.riak.client.core.query.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import reactor.core.Exceptions;

@Repository
public class UrlRepository {

    @Autowired
    private RiakClient client;
    @Value("${application.riak.bucket_type}")
    private String bucketType;
    @Value("${application.riak.bucket_name}")
    private String bucketName;
    @Value("${application.riak.fetch_timeout}")
    private int fetchTimeout;
    private Namespace bucket;

    @PostConstruct
    public void init() {
        bucket = new Namespace(bucketType, bucketName);
    }

    @Cacheable(URL_CACHE_NAME)
    public String resolve(String key) throws ExecutionException, InterruptedException {
        var location = new Location(bucket, key);
        var fetch = new FetchValue.Builder(location)
                .withOption(FetchValue.Option.NOTFOUND_OK, false)
                .withOption(FetchValue.Option.R, Quorum.oneQuorum())
                .withTimeout(fetchTimeout)
                .build();
        var response = client.execute(fetch);
        return response.getValue(String.class);
    }
}

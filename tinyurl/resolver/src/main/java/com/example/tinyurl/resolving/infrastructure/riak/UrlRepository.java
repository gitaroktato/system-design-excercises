package com.example.tinyurl.resolving.infrastructure.riak;

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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository
public class UrlRepository {

    @Autowired
    private RiakClient client;
    @Value("${application.riak.bucket_type}")
    private String bucketType;
    @Value("${application.riak.bucket_name}")
    private String bucketName;
    private Namespace bucket;

    @PostConstruct
    public void init() {
        bucket = new Namespace(bucketType, bucketName);
    }

    @Cacheable("URLs")
    public String resolve(String key) throws ExecutionException, InterruptedException {
        var location = new Location(bucket, key);
        var fetch = new FetchValue.Builder(location)
                .withOption(FetchValue.Option.NOTFOUND_OK, false)
                .withOption(FetchValue.Option.R, Quorum.oneQuorum())
                .build();
        var response = client.execute(fetch);
        return response.getValue(String.class);
    }
}

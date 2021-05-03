package com.example.tinyurl.shortening.infrastructure.riak;

import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;

import com.basho.riak.client.api.RiakClient;
import com.basho.riak.client.api.commands.kv.StoreValue;
import com.basho.riak.client.core.query.Location;
import com.basho.riak.client.core.query.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    public void save(String key, String url) throws ExecutionException, InterruptedException {
        var location = new Location(bucket, key);
        var storeOp = new StoreValue.Builder(url)
                .withLocation(location)
                .withOption(StoreValue.Option.RETURN_BODY, true)
                .build();
        client.execute(storeOp);
    }
}

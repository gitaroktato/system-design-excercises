package com.example.tinyurl;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.basho.riak.client.api.RiakClient;
import com.basho.riak.client.api.cap.ConflictResolver;
import com.basho.riak.client.api.cap.ConflictResolverFactory;
import com.basho.riak.client.api.cap.Quorum;
import com.basho.riak.client.api.cap.UnresolvedConflictException;
import com.basho.riak.client.api.commands.kv.FetchValue;
import com.basho.riak.client.api.commands.kv.StoreValue;
import com.basho.riak.client.core.RiakCluster;
import com.basho.riak.client.core.RiakNode;
import com.basho.riak.client.core.query.Location;
import com.basho.riak.client.core.query.Namespace;
import com.basho.riak.client.core.query.RiakObject;
import com.basho.riak.client.core.util.BinaryValue;
import com.example.tinyurl.shortening.api.dto.UrlDto;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("learning")
public class RiakConnectivityTest {

    // This will create a client object that we can use to interact with Riak
    private static RiakCluster setUpCluster() {
        var node = new RiakNode.Builder()
                .withRemoteAddress("192.168.99.100")
                .withRemotePort(8087)
                .build();

        // This cluster object takes our one node as an argument
        var cluster = new RiakCluster.Builder(node)
                .build();

        // The cluster must be started to work, otherwise you will see errors
        cluster.start();
        return cluster;
    }

    @Test
    public void testCreate() throws ExecutionException, InterruptedException {
        var client = new RiakClient(setUpCluster());
        Namespace bucket = new Namespace("tinyurl", "url");
        var url = new UrlDto();
        url.originalUrl = "https://google.com/12345";
        url.alias = "http://localhost:8081/12345";
        Location newUrl = new Location(bucket, "new");
        StoreValue storeOp = new StoreValue.Builder(url)
                .withLocation(newUrl)
                .withOption(StoreValue.Option.RETURN_BODY, true)
                .build();
        var resp = client.execute(storeOp);
        assertThat(resp.getLocation()).isEqualTo(newUrl);
        assertThat(resp.getValue(UrlDto.class).originalUrl).isEqualTo(url.originalUrl);
    }

    @Test
    public void testCreateWithAutomaticKey() throws ExecutionException, InterruptedException {
        var client = new RiakClient(setUpCluster());
        Namespace bucket = new Namespace("tinyurl", "url");
        var url = new UrlDto();
        url.originalUrl = "https://google.com/12345";
        url.alias = "http://localhost:8081/12345";
        StoreValue storeOp = new StoreValue.Builder(url)
                .withNamespace(bucket)
                .withOption(StoreValue.Option.RETURN_BODY, true)
                .build();
        var resp = client.execute(storeOp);
        assertThat(resp.getLocation().getNamespace()).isEqualTo(bucket);
        System.out.println(resp.getGeneratedKey().toString());
        assertThat(resp.getGeneratedKey().toString()).isNotEmpty();
        assertThat(resp.getValue(UrlDto.class).originalUrl).isEqualTo(url.originalUrl);
    }

    @Test
    public void testFetch() throws ExecutionException, InterruptedException {
        var client = new RiakClient(setUpCluster());
        Location myKey = new Location(new Namespace("tinyurl", "url"), "new2");
        FetchValue fetch = new FetchValue.Builder(myKey)
                .build();
        FetchValue.Response response = client.execute(fetch);
        UrlDto obj = response.getValue(UrlDto.class);
        System.out.println(obj);
    }

    @Test
    public void testFetchWithConflict() throws ExecutionException, InterruptedException {
        var client = new RiakClient(setUpCluster());
        ConflictResolverFactory factory = ConflictResolverFactory.getInstance();
        factory.registerConflictResolver(UrlDto.class, new UrlDtoConflictResolver());
        Location myKey = new Location(new Namespace("tinyurl", "url"), "conflict_new");
        FetchValue fetch = new FetchValue.Builder(myKey)
                .build();
        FetchValue.Response response = client.execute(fetch);
        UrlDto obj = response.getValue(UrlDto.class);
        System.out.println(obj);
    }

    @Test
    public void testCreateWithConflictResolver() throws ExecutionException, InterruptedException {
        var client = new RiakClient(setUpCluster());
        ConflictResolverFactory factory = ConflictResolverFactory.getInstance();
        factory.registerConflictResolver(UrlDto.class, new UrlDtoPreventingConflictResolver());
        Namespace bucket = new Namespace("tinyurl", "url");
        var url = new UrlDto();
        url.originalUrl = "https://google.com/conflict2";
        url.alias = "http://localhost:8081/conflict2";
        Location newUrl = new Location(bucket, "conflict_new");
        StoreValue storeOp = new StoreValue.Builder(url)
                .withLocation(newUrl)
                .withOption(StoreValue.Option.RETURN_BODY, true)
                .withOption(StoreValue.Option.PW, Quorum.quorumQuorum())
                .build();
        var resp = client.execute(storeOp);
        assertThat(resp.getLocation()).isEqualTo(newUrl);
        assertThat(resp.getValue(UrlDto.class).originalUrl).isEqualTo(url.originalUrl);
    }


    public static class UrlDtoPreventingConflictResolver implements ConflictResolver<UrlDto> {

        @Override
        public UrlDto resolve(List<UrlDto> dtos) throws UnresolvedConflictException {
            throw new RuntimeException("Multiple values not allowed");
        }
    }

    public static class UrlDtoConflictResolver implements ConflictResolver<UrlDto> {

        @Override
        public UrlDto resolve(List<UrlDto> dtos) throws UnresolvedConflictException {
            System.out.println(dtos);
            return dtos.get(0);
        }
    }

}

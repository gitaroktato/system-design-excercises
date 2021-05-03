package com.example.tinyurl;

import static org.assertj.core.api.Assertions.assertThat;

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
import com.example.tinyurl.shortening.api.dto.UrlDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

@Tag("learning")
@Disabled
public class RiakConnectivityTest {

    public static final DockerImageName RIAK_DOCKER_IMAGE = DockerImageName.parse("basho/riak-kv");
    public static final int RIAK_PORT = 8087;
    public static final String BUCKET_TYPE = "default";
    private static final Integer RIAK_API_PORT = 8098;

    private static final GenericContainer<?> riakContainer;

    static {
        riakContainer = new GenericContainer<>(RIAK_DOCKER_IMAGE)
                .withExposedPorts(RIAK_PORT, RIAK_API_PORT)
                .waitingFor(Wait.forHttp("/buckets?buckets=true").forPort(RIAK_API_PORT));
        riakContainer.start();
    }

    private Integer riakPort;
    private String riakHost;

    @BeforeEach
    public void startRiakCluster() {
        this.riakPort = riakContainer.getMappedPort(RIAK_PORT);
        this.riakHost = riakContainer.getHost();
        System.out.println(" ===== Riak access point ===== " + this.riakHost + ":" + this.riakPort + " ===== ");
    }

    // This will create a client object that we can use to interact with Riak
    private RiakCluster setUpCluster() {
        var node = new RiakNode.Builder()
                .withRemoteAddress(riakHost)
                .withRemotePort(riakPort)
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
        Namespace bucket = new Namespace(BUCKET_TYPE, "url");
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
        Namespace bucket = new Namespace(BUCKET_TYPE, "url");
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
        Location myKey = new Location(new Namespace(BUCKET_TYPE, "url"), "new2");
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
        Location myKey = new Location(new Namespace(BUCKET_TYPE, "url"), "conflict_new");
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
        Namespace bucket = new Namespace(BUCKET_TYPE, "url");
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

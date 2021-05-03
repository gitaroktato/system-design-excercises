package com.example.tinyurl.resolving.infrastructure.spring;

import com.basho.riak.client.api.RiakClient;
import com.basho.riak.client.core.RiakCluster;
import com.basho.riak.client.core.RiakNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RiakConfiguration {

    @Value("${application.riak.host}")
    private String riakHost;
    @Value("${application.riak.port}")
    private int riakPort;

    @Bean
    public RiakCluster createCluster() {
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

    @Bean
    public RiakClient createClient(RiakCluster cluster) {
        return new RiakClient(cluster);
    }
}

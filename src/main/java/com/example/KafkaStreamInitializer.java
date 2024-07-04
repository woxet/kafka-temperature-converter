package com.example;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.Topology;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Properties;

@ApplicationScoped
public class KafkaStreamInitializer {

    @ConfigProperty(name = "kafka.bootstrap.servers")
    String bootstrapServers;

    @Inject
    Topology topology;

    public void onStart() {
        KafkaStreams streams = new KafkaStreams(topology, getStreamsConfig());
        streams.start();
    }

    private Properties getStreamsConfig() {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("application.id", "temperature-converter");
        props.put("default.key.serde", Serdes.String().getClass());
        props.put("default.value.serde", Serdes.Integer().getClass());
        return props;
    }
}

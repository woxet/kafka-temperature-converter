package com.example;

import io.quarkus.runtime.StartupEvent;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.Topology;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.Properties;

@ApplicationScoped
public class KafkaStreamConfig {

    @ConfigProperty(name = "kafka.bootstrap.servers")
    String bootstrapServers;

    @Inject
    Topology topology;

    void onStart(@Observes StartupEvent ev) {
        KafkaStreams streams = new KafkaStreams(topology, getStreamsConfig());
        streams.start();
    }

    private Properties getStreamsConfig() {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("application.id", "temperature-converter");
        props.put("default.key.serde", org.apache.kafka.common.serialization.Serdes.String().getClass().getName());
        props.put("default.value.serde", org.apache.kafka.common.serialization.Serdes.Integer().getClass().getName());
        return props;
    }
}

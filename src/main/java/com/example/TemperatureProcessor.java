package com.example;

import io.quarkus.runtime.StartupEvent;
import javax.inject.Inject;

import io.smallrye.reactive.messaging.annotations.Blocking;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.util.Properties;

@ApplicationScoped
public class TemperatureProcessor {

    StreamsBuilder streamsBuilder;

    public void onStart(@Observes StartupEvent event) {
        createTopology();
    }

    private void createTopology() {
        KStream<String, Integer> sourceStream = streamsBuilder.stream("temperature-celsius-topic");

        KStream<String, Integer> convertedStream = sourceStream.mapValues(celsius -> celsiusToFahrenheit(celsius));

        convertedStream.to("temperature-fahrenheit-topic", Produced.with(Serdes.String(), Serdes.Integer()));

        Topology topology = streamsBuilder.build();
        KafkaStreams kafkaStreams = new KafkaStreams(topology, getProperties());
        kafkaStreams.start();
    }

    private int celsiusToFahrenheit(int celsius) {
        return (int) (celsius * 9.0 / 5.0 + 32);
    }

    private Properties getProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "temperature-conversion");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.Integer().getClass());
        return props;
    }
}

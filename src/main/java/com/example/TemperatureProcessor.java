package com.example;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;

import java.time.Instant;
import java.util.Properties;

public class TemperatureProcessor {
    public final String CELSIUS_TOPIC = "celsius-topic";
    public final String FAHRENHEIT_TOPIC = "fahrenheit-topic";

    public void process(){
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-temperature-converter");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, Double> kStream = builder.stream(CELSIUS_TOPIC, Consumed.with(Serdes.String(), Serdes.Double()))
                .map((key, value) -> new KeyValue<>(key, (value * 9/5) + 32))
                .peek((k, v) -> System.out.println("Key = " + k + " Value = " + v))
                ;

        kStream.to(FAHRENHEIT_TOPIC);

        Topology topology = builder.build();

        KafkaStreams streams = new KafkaStreams(topology, props);
        streams.start();
    }

}


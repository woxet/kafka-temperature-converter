package fr.master.isri;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Properties;

public class TemperatureProcessor {

    public final String CELSIUS_TOPIC = "celsius-topic";
    public final String FAHRENHEIT_TOPIC = "fahrenheit-topic";

    public void process() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-temperature-converter");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv("KAFKA_BOOTSTRAP_SERVERS"));
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, Double> kStream = builder.stream(CELSIUS_TOPIC, Consumed.with(Serdes.String(), Serdes.Double()))
                .map((key, value) -> new KeyValue<>(key, (value * 9/5) + 32))
                .peek((key, value) -> System.out.println("Key = " + key + " Value = " + value));

        kStream.to(FAHRENHEIT_TOPIC, Produced.with(Serdes.String(), Serdes.Double()));

        Topology topology = builder.build();

        KafkaStreams streams = new KafkaStreams(topology, props);
        streams.start();
    }
}

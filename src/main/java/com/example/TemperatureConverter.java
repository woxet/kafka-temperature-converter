package com.example;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class TemperatureConverter {

    @Produces
    public Topology buildTopology() {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, Integer> celsiusStream = builder.stream("celsius");

        KStream<String, Integer> fahrenheitStream = celsiusStream.mapValues(tempCelsius -> (tempCelsius * 9 / 5) + 32);
        fahrenheitStream.to("fahrenheit");

        return builder.build();
    }
}

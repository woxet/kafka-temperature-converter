package com.example;

import io.smallrye.reactive.messaging.annotations.Channel;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.ConcurrentLinkedQueue;

@ApplicationScoped
public class TemperatureConsumer {

    private ConcurrentLinkedQueue<Integer> temperatureQueue = new ConcurrentLinkedQueue<>();

    @Inject
    @Channel("temperature-fahrenheit")
    org.eclipse.microprofile.reactive.messaging.Message<Integer> message;

    @Incoming("temperature-fahrenheit")
    public void consumeTemperature(Integer temperature) {
        temperatureQueue.offer(temperature);
    }

    public String getLatestTemperature() {
        Integer latestTemperature = temperatureQueue.peek();
        return latestTemperature != null ? latestTemperature.toString() : "No temperature data available";
    }
}

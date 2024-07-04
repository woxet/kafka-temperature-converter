package com.example;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.ConcurrentLinkedQueue;

@ApplicationScoped
public class TemperatureConsumer {

    private final ConcurrentLinkedQueue<Integer> temperatureQueue = new ConcurrentLinkedQueue<>();

    @Incoming("temperature-fahrenheit")
    public void consumeTemperature(Integer temperature) {
        temperatureQueue.offer(temperature);
    }

    public String getLatestTemperature() {
        Integer latestTemperature = temperatureQueue.peek();
        return latestTemperature != null ? latestTemperature.toString() : "No temperature data available";
    }
}

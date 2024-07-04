package com.example;

import io.smallrye.reactive.messaging.annotations.Channel;
import io.smallrye.reactive.messaging.annotations.Emitter;
import io.vertx.mutiny.core.Vertx;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Random;

@ApplicationScoped
public class TemperatureProducer {

    @Inject
    @Channel("celsius")
    Emitter<Integer> emitter;

    @Inject
    Vertx vertx;

    Random random = new Random();

    public void generate() {
        vertx.setPeriodic(5000, id -> {
            int tempCelsius = random.nextInt(40); // Génère une température aléatoire en Celsius
            emitter.send(tempCelsius);
        });
    }
}

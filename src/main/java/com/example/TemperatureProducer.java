package com.example;

import io.quarkus.scheduler.Scheduled;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.enterprise.context.ApplicationScoped;
import java.util.Properties;
import java.util.Random;

@ApplicationScoped
public class TemperatureProducer {

    private final Producer<String, String> kafkaProducer;
    private final Random random = new Random();

    KafkaProducerConfig kafkaProducerConfig;

    public TemperatureProducer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaProducerConfig.getBootstrapServers());
        props.put("key.serializer", kafkaProducerConfig.getKeySerializer());
        props.put("value.serializer", kafkaProducerConfig.getValueSerializer());

        kafkaProducer = new KafkaProducer<>(props);
    }

    @Scheduled(every = "5s")
    void sendTemperature() {
        int temperature = random.nextInt(40); // Génère une température aléatoire entre 0 et 40 degrés Celsius
        String messageKey = "temperature";
        String messageValue = "{\"celsius\": " + temperature + "}";

        kafkaProducer.send(new ProducerRecord<>("temperature-celsius-topic", messageKey, messageValue),
                (metadata, exception) -> {
                    if (exception != null) {
                        System.err.println("Error sending message: " + exception.getMessage());
                    } else {
                        System.out.println("Sent temperature: " + temperature);
                    }
                });
    }

    public void close() {
        kafkaProducer.close();
    }
}

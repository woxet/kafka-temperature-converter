package fr.master.isri;

import io.quarkus.runtime.Startup;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.kafka.streams.StreamsConfig;

import java.util.Properties;
import java.util.Random;

@Startup
@ApplicationScoped
public class TemperatureProducer implements Runnable {

    private static final String TOPIC_NAME = "temperature-celsius"; // Le topic Kafka où les températures en Celsius seront publiées

    private final KafkaProducer<String, String> kafkaProducer;
    private final Random random;

    @Inject
    public TemperatureProducer() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-temperature-converter");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv("KAFKA_BOOTSTRAP_SERVERS"));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        this.kafkaProducer = new KafkaProducer<>(props);
        this.random = new Random();
    }

    @Override
    public void run() {
        try {
            while (true) {
                double temperature = generateTemperature();
                sendMessage(Double.toString(temperature));
                Thread.sleep(5000); // Envoyer une nouvelle température toutes les 5 secondes
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            kafkaProducer.close();
        }
    }

    private double generateTemperature() {
        // Générer une température aléatoire entre -20°C et 40°C pour simulation
        return random.nextDouble() * 60 - 20;
    }

    private void sendMessage(String temperature) {
        kafkaProducer.send(new ProducerRecord<>(TOPIC_NAME, temperature));
        System.out.println("Sent temperature: " + temperature + " °C");
    }
}

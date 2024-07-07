package fr.master.isri;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.apache.kafka.streams.StreamsConfig;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@ApplicationScoped
@Path("/temperature")
public class TemperatureConsumer {

    private final KafkaConsumer<String, String> consumer;

    public TemperatureConsumer() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-temperature-converter");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv("KAFKA_BOOTSTRAP_SERVERS"));
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "temperature-consumer");

        this.consumer = new KafkaConsumer<>(props);
        this.consumer.subscribe(Collections.singleton("fahrenheit-topic"));
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String consumeTemperature() {
        try {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            StringBuilder response = new StringBuilder();

            records.forEach(record -> {
                response.append("Consumed record with key ")
                        .append(record.key())
                        .append(" and value ")
                        .append(record.value())
                        .append("\n");
            });

            return response.toString();
        } catch (WakeupException e) {
            // Handle wake-up scenario (e.g., shutdown)
            return "Consumer has been woken up.";
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
            return "Error while consuming temperatures: " + e.getMessage();
        }
    }

    public void close() {
        consumer.close();
    }
}

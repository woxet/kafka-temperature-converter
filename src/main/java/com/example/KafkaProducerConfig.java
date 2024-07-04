package com.example;

import org.eclipse.microprofile.config.inject.ConfigProperties;

@ConfigProperties(prefix = "kafka")
public class KafkaProducerConfig {

    private String bootstrapServers;
    private String keySerializer;
    private String valueSerializer;
    private String groupId; // Exemple d'une autre propriété Kafka

    // Ajoutez ici les autres propriétés Kafka nécessaires

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getKeySerializer() {
        return keySerializer;
    }

    public void setKeySerializer(String keySerializer) {
        this.keySerializer = keySerializer;
    }

    public String getValueSerializer() {
        return valueSerializer;
    }

    public void setValueSerializer(String valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}

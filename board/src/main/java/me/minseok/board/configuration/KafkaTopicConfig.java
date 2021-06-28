package me.minseok.board.configuration;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {
    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value(value = "${kafka.predict.bootstrapAddress}")
    private String predictBootstrapAddress;

    @Value(value = "${kafka.my.comment.topic.name}")
    private String topicName;

    @Value(value = "${kafka.predict.topic.name}")
    private String predictTopic;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, predictBootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topic(){
        return new NewTopic(topicName,1,(short)1);
    }

    @Bean
    public NewTopic topic1(){
        return new NewTopic(predictTopic,1,(short)1);
    }



}

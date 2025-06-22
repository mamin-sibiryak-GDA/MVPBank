package com.example.mvpbank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service // Указывает, что это сервис Spring
@RequiredArgsConstructor // Lombok создаёт конструктор с final полями
public class KafkaSenderService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    // Метод для отправки сообщений в заданную тему Kafka
    public void sendMessage(String topic, Object data) {
        kafkaTemplate.send(topic, data); // Отправка сообщения (сериализуется как JSON)
    }
}

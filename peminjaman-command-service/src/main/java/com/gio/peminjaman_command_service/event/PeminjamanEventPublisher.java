package com.gio.peminjaman_command_service.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gio.peminjaman_command_service.model.Peminjaman;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PeminjamanEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Value("${app.rabbitmq-peminjaman.exchange}")
    private String exchange;

    @Value("${app.rabbitmq-peminjaman.routing-key}")
    private String routingKey;

    public PeminjamanEventPublisher(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishPeminjamanEvent(Peminjaman peminjaman) {
        try {
            String message = objectMapper.writeValueAsString(peminjaman);
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
            System.out.println("📤 Event dikirim ke RabbitMQ: " + message);
        } catch (Exception e) {
            System.err.println("❌ Gagal mengirim event ke RabbitMQ: " + e.getMessage());
        }
    }
}

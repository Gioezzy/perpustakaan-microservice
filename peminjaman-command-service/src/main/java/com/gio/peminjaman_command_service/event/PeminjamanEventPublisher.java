package com.gio.peminjaman_command_service.event;

import com.gio.peminjaman_command_service.model.Peminjaman;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PeminjamanEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq-peminjaman.exchange}")
    private String exchange;

    @Value("${app.rabbitmq-peminjaman.routing-key}")
    private String routingKey;

    public PeminjamanEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishPeminjamanEvent(Peminjaman peminjaman) {
        try {
            System.out.println("📤 Mengirim event ke exchange: " + exchange + " dengan routing key: " + routingKey);
            rabbitTemplate.convertAndSend(exchange, routingKey, peminjaman);
            System.out.println("✅ Event dikirim ke RabbitMQ: " + peminjaman);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Gagal mengirim event ke RabbitMQ: " + e.getMessage());
        }
    }
}

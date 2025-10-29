package com.gio.peminjaman_command_service.listener;

// import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PeminjamanEventListener {

    // @RabbitListener(queues = "${app.rabbitmq-peminjaman.queue}")
    // public void handlePeminjamanEvent(Object message) {
    //     System.out.println("📨 Received event: " + message.toString());
    // }
}

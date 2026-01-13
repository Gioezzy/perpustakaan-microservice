package com.gio.peminjaman_query_service.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gio.peminjaman_query_service.config.RabbitMQConfig;
import com.gio.peminjaman_query_service.model.Peminjaman;
import com.gio.peminjaman_query_service.service.PeminjamanService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PeminjamanEventListener {

    private final PeminjamanService service;

    public PeminjamanEventListener(PeminjamanService service, ObjectMapper objectMapper) {
        // this.objectMapper = objectMapper;
        this.service = service;
    }
    // private final ObjectMapper objectMapper;

    // @RabbitListener(queues = RabbitMQConfig.QUEUE)
    // public void handlePeminjamanEvent(String message) {
    //     try {
    //         Peminjaman peminjaman = objectMapper.readValue(message, Peminjaman.class);
    //         service.save(peminjaman);
    //         System.out.println("📨 Event diterima dan disimpan ke DB Query: " + peminjaman);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void handlePeminjamanEvent(Peminjaman peminjaman) {
        try {
            service.save(peminjaman);
            System.out.println("📥 Event diterima dan disimpan ke DB Query: " + peminjaman);
        } catch (Exception e) {
            System.err.println("❌ Error processing Peminjaman Event: " + e.getMessage());
            e.printStackTrace();
        }
    }

}

package com.gio.rabbitmq_peminjaman_service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.gio.rabbitmq_peminjaman_service.vo.Anggota;
import com.gio.rabbitmq_peminjaman_service.vo.Peminjaman;
import com.gio.rabbitmq_peminjaman_service.vo.ResponseTemplate;

@Service
public class PeminjamanCostumerService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.mail.from}")
    private String from;

    @RabbitListener(queues = "${app.rabbitmq-peminjaman.queue}")

    @Transactional
    public void receiveOrder(@Payload Peminjaman peminjaman) {
        System.out.println("📩 Received message from queue with ID: " + peminjaman.getId());

        if (peminjaman.getId() == null) {
            System.err.println("⚠️ ID peminjaman null, email tidak dikirim.");
            return;
        }

        try {
            // Adding a delay to mitigate race condition
            Thread.sleep(2000);

            ServiceInstance serviceInstance = discoveryClient
                    .getInstances("API-GATEWAY-PUSTAKA")
                    .get(0);

            String url = serviceInstance.getUri() + "/api/peminjaman/query/" + peminjaman.getId() + "/detail";
            ResponseTemplate dataPeminjaman = restTemplate.getForObject(url, ResponseTemplate.class);

            if (dataPeminjaman == null) {
                System.err.println("⚠️ Data peminjaman tidak ditemukan untuk ID: " + peminjaman.getId());
                return;
            }

            Anggota anggota = dataPeminjaman.getAnggota();
            String email = anggota.getEmail();

            System.out.println("✉️ Sending notification to email: " + email);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(from);
            mailMessage.setTo(email);
            mailMessage.setSubject("Konfirmasi Peminjaman Buku Berhasil");
            mailMessage.setText(dataPeminjaman.sendMailMessage());
            javaMailSender.send(mailMessage);

            System.out.println("✅ Email berhasil dikirim ke " + email);

        } catch (Exception e) {
            System.err.println("❌ Error saat mengirim email peminjaman: " + e.getMessage());
        }
    }
}

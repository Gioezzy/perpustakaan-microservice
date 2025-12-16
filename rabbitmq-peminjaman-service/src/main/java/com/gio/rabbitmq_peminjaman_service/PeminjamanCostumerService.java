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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.gio.rabbitmq_peminjaman_service.vo.Anggota;
import com.gio.rabbitmq_peminjaman_service.vo.Peminjaman;

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
            Thread.sleep(3000);


            ServiceInstance gatewayInstance = discoveryClient
                    .getInstances("API-GATEWAY-PUSTAKA")
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Gateway service not found!"));

            String urlPeminjaman = gatewayInstance.getUri() + "/api/peminjaman/query/" + peminjaman.getId() + "/detail";
            Peminjaman dataPeminjaman = null;

            int retryCount = 0;
            while (retryCount < 3) {
                try {
                    dataPeminjaman = restTemplate.getForObject(urlPeminjaman, Peminjaman.class);
                    if (dataPeminjaman != null) break;
                } catch (HttpClientErrorException.NotFound e) {
                    System.out.println("⚠️ Data peminjaman belum tersedia, retry ke-" + (retryCount + 1));
                    Thread.sleep(2000);
                }
                retryCount++;
            }

            if (dataPeminjaman == null) {
                System.err.println("❌ Data peminjaman tidak ditemukan untuk ID: " + peminjaman.getId());
                return;
            }

            String urlAnggota = gatewayInstance.getUri() + "/api/anggota/" + dataPeminjaman.getAnggotaId();
            Anggota anggota = restTemplate.getForObject(urlAnggota, Anggota.class);

            if (anggota == null) {
                System.err.println("❌ Data anggota tidak ditemukan untuk ID: " + dataPeminjaman.getAnggotaId());
                return;
            }

            String email = anggota.getEmail();
            System.out.println("✉️ Sending notification to email: " + email);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(from);
            mailMessage.setTo(email);
            mailMessage.setSubject("Konfirmasi Peminjaman Buku Berhasil");
            mailMessage.setText(
                "Halo " + anggota.getNama() + ",\n\n" +
                "Peminjaman buku Anda telah berhasil diproses dengan rincian berikut:\n" +
                "📘 ID Peminjaman: " + dataPeminjaman.getId() + "\n" +
                "📅 Tanggal Peminjaman: " + dataPeminjaman.getTanggalPinjam() + "\n" +
                "📆 Tanggal Kembali: " + dataPeminjaman.getTanggalKembali() + "\n\n" +
                "Terima kasih telah menggunakan layanan kami.\n\nSalam,\nTim Perpustakaan"
            );

            javaMailSender.send(mailMessage);
            System.out.println("✅ Email berhasil dikirim ke " + email);

        } catch (Exception e) {
            System.err.println("❌ Error saat mengirim email peminjaman: " + e.getMessage());
        }
    }
}

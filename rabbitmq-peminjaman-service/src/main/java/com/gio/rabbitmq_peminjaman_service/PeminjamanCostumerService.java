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
        System.out.println("Sending receive to email: " + peminjaman.getId());
        
        try {
            ServiceInstance serviceInstance = discoveryClient.getInstances("API-GATEWAY-PUSTAKA").get(0);
            ResponseTemplate[] response = restTemplate.getForObject(serviceInstance.getUri() + "/api/peminjaman/" + peminjaman.getId() + "/detail", ResponseTemplate[].class);
            ResponseTemplate dataPeminjaman = response[0];
            Anggota anggota = dataPeminjaman.getAnggota();
            String email = anggota.getEmail();

            System.out.println("Sending notification to email: " + email);
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(from);
            mailMessage.setTo(email);
            mailMessage.setText(dataPeminjaman.sendMailMessage());
            mailMessage.setSubject("Konfirmasi Peminjaman Buku Berhasil");
            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    
    }
}

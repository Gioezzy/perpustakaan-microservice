package com.gio.rabbitmq_pengembalian_service;

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

import com.gio.rabbitmq_pengembalian_service.vo.Anggota;
import com.gio.rabbitmq_pengembalian_service.vo.Pengembalian;
import com.gio.rabbitmq_pengembalian_service.vo.ResponseTemplate;

@Service
public class PengembalianConsumerService {

  @Autowired
  private JavaMailSender javaMailSender;

  @Autowired
  private DiscoveryClient discoveryClient;

  @Autowired
  private RestTemplate restTemplate;

  @Value("${app.mail.from}")
  private String from;

  @RabbitListener(queues = "${app.rabbitmq-pengembalian.queue}")

  @Transactional
  public void receiveOrder(@Payload Pengembalian pengembalian){
    System.out.println("📩 Received message from queue with ID: " + pengembalian.getId());

    try{
      ServiceInstance serviceInstance = discoveryClient.getInstances("API-GATEWAY-PUSTAKA").get(0);
      ResponseTemplate[] response = restTemplate.getForObject(serviceInstance.getUri() + "/api/pengembalian/" + pengembalian.getId() + "/detail", ResponseTemplate[].class);

      if (response == null || response.length == 0) {
        System.err.println("⚠️ Data pengembalian tidak ditemukan untuk ID: " + pengembalian.getId());
        return;
      }

      ResponseTemplate dataPengembalian = response[0];
      Anggota anggota = dataPengembalian.getAnggota();
      String email = anggota.getEmail();

      System.out.println("Sending notification to email:" + email);
      SimpleMailMessage mailMessage = new SimpleMailMessage();
      mailMessage.setFrom(from);
      mailMessage.setTo(email);
      mailMessage.setText(dataPengembalian.sendMailMessage());
      mailMessage.setSubject("Konfirmasi Pengembalian Buku Berhasil");
      javaMailSender.send(mailMessage);

      System.out.println("✅ Email berhasil dikirim ke " + email);
      
    }catch (Exception e){
      System.err.println("❌ Error saat mengirim email pengembalian: " + e.getMessage());
    }
  }
}

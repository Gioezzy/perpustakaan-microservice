package com.gio.peminjaman_query_service;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class PeminjamanQueryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PeminjamanQueryServiceApplication.class, args);
	}

}

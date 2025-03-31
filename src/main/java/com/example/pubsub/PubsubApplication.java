package com.example.pubsub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PubsubApplication {
	public static void main(String[] args) {
		System.out.println("🚀 Iniciando a aplicação PubSub...");
		SpringApplication.run(PubsubApplication.class, args);
		System.out.println("✅ Aplicação iniciada com sucesso!");
	}
}
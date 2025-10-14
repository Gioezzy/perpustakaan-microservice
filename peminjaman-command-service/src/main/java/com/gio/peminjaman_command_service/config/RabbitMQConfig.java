package com.gio.peminjaman_command_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class RabbitMQConfig {

  @Value("${app.rabbitmq-peminjaman.queue}")
  private String queueName;

  @Value("${app.rabbitmq-peminjaman.exchange}")
  private String exchange;

  @Value("${app.rabbitmq-peminjaman.routing-key}")
  private String routingKey;

  @Bean
  public Queue queue() {
    return new Queue(queueName, true);
  }

  @Bean
  public DirectExchange exchange(){
    return new DirectExchange(exchange);
  }

  @Bean
  public Binding binding(Queue queue, DirectExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(routingKey);
  }

  @Bean
  public Jackson2JsonMessageConverter messageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(messageConverter());
    return template;
  }

}

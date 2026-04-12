package com.example.fjm0313_takeout_self.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.cglib.core.Converter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitMQConfig {

    // 交换机名称
    public static final String SECKILL_EXCHANGE = "seckill.exchange";

    // 队列名称
    public static final String SECKILL_ORDER_QUEUE = "seckill.order.queue";

    // 路由键
    public static final String SECKILL_ORDER_ROUTING_KEY = "seckill.order";


    @Bean
    public Queue seckillOrderQueue(){
        return new Queue(SECKILL_ORDER_QUEUE,true);
    }

    @Bean
    public DirectExchange seckillExchange(){
        return new DirectExchange(SECKILL_EXCHANGE);

    }

    @Bean
    public Binding seckillOrderBinding(Queue seckillOrderQueue, DirectExchange seckillExchange){
        return BindingBuilder.bind(seckillOrderQueue).to(seckillExchange).with(SECKILL_ORDER_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}

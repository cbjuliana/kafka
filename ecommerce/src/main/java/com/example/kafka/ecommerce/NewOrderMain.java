package com.example.kafka.ecommerce;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.common.Uuid;

public class NewOrderMain {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
		try (var orderDispatcher = new KafkaDispatcher<Order>()) {
			
			try (var emailDispatcher = new KafkaDispatcher<String>()) {
		
				for (var i = 0; i < 10; i++) {
					var userId = Uuid.randomUuid().toString();
					var orderId = Uuid.randomUuid().toString();
					var amount = new BigDecimal(Math.random() * 5000 + 1);
					var order = new Order(userId, orderId, amount);	
					orderDispatcher.send("ECOMMERCE_NEW_ORDER", userId, order);				
					
					var email = "Thak you for your order. We are processing your order.";	
					emailDispatcher.send("ECOMMERCE_SEND_EMAIL", userId, email);	
				}
				
			}
			
		}
		
	}

}

package com.example.kafka.ecommerce;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.Uuid;
import org.apache.kafka.common.serialization.StringSerializer;

public class NewOrderMain {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
		var producer = new KafkaProducer<String, String>(properties());
		
		for (var i = 0; i < 100; i++) {
			var key = Uuid.randomUuid().toString();
			
			Callback callback = (data, ex) -> {
				if (ex != null) {
					ex.printStackTrace();
					return;
				}
				System.out.println("Sucesso enviando " + data.topic() + " :::partition " + data.partition() + " / offset " + data.offset() + "/timestamp " + data.timestamp());
			};
			
			var order = key + ",67523,7894589745";
			var orderRecord = new ProducerRecord<>("ECOMMERCE_NEW_ORDER", key, order);		
			producer.send(orderRecord, callback).get();	
			
			var email = "Thak you for your order. We are processing your order.";
			var emailRecord = new ProducerRecord<>("ECOMMERCE_SEND_EMAIL", key, email);		
			producer.send(emailRecord, callback).get();	
		}
	}
	
	private static Properties properties() {
		var properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		return properties;
	}

}

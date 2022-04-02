package com.bav.testproject;

import com.bav.testproject.database.User;
import com.mongodb.client.MongoClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootApplication
public class TestProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestProjectApplication.class, args);
	}

	@Bean
	public MongoTemplate mongoTemplate() {
		return new MongoTemplate(MongoClients.create("mongodb://localhost:27017"), "test");
	}

	@Bean
	public User user(){
		return new User();
	}

}

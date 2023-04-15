package com.pthore.service.posts.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongodbConfigs {
	
	
	@Bean
	public MongoClient mongoClient() {
		return MongoClients.create("mongodb://localhost:27017");
		// mongodb://mongoUser:mongo123___@localhost:27017/?authSource=admin
	}
	
	@Bean
	public MongoTemplate mongoTemplate(MongoClient mongoClient) {
		
		MongoDatabaseFactory dbFactory = new SimpleMongoClientDatabaseFactory(mongoClient, "pthore");
		MongoTemplate template = new MongoTemplate(dbFactory);
		return template;
	}
}

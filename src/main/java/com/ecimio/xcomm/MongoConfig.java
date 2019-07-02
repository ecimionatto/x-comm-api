package com.ecimio.xcomm;


import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.ecimio.xcomm.*")
public class MongoConfig extends AbstractReactiveMongoConfiguration {

    @Value("${mongo.port}")
    private String port;

    @Value("${mongo.dbname}")
    private String dbName;

    @Value("${mongo.hostname}")
    private String hostname;

    @Override
    public MongoClient reactiveMongoClient() {
        String myHost = System.getenv("MONGODB_HOST");
        return MongoClients.create("mongodb://" + (myHost == null ? hostname : myHost) + ":" + port);
    }

    @Override
    protected String getDatabaseName() {
        return dbName;
    }

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate() {
        return new ReactiveMongoTemplate(reactiveMongoClient(), getDatabaseName());
    }
}

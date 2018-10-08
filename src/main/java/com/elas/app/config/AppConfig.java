package com.elas.app.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({"classpath:config.properties"})
public class AppConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);
	
	@Value("${elasticsearch.address}")
	private String esclusterAddress;
	
	@Value("${elasticsearch.portA}")
	private Integer portA;
	
	@Value("${elasticsearch.portB}")
	private Integer portB;
	
	@Bean("restHighLevelClient")
	public RestHighLevelClient getRestHighLevelClient() {
		LOGGER.info("Initializing Elastic Search transport");
		RestHighLevelClient client = null; 
		try {
			client = new RestHighLevelClient(RestClient.builder(
			        new HttpHost(this.esclusterAddress, portA, "http"),
			        new HttpHost(this.esclusterAddress, portB, "http")));
		}catch (Exception e) {
			LOGGER.error("Elastic Client creation error {}", e);
		}
		LOGGER.info("Elastic Client Created");
		return client;
	}
}

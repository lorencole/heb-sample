package com.l.heb;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.l.heb.integration.GcpVision;
import com.l.heb.model.ImageRepository;
import com.l.heb.utils.ExifUtil;

@SpringBootApplication
@EnableMongoRepositories
public class HebSampleApplication extends SpringBootServletInitializer {

	// ******** Data repositories ***************
	@Autowired
	ImageRepository imageRepo;
	
	// ******** Mongodb configuration *********** 
	@Autowired
	private MappingMongoConverter mongoConverter;
	
	@Autowired
	public MongoTemplate mongoTemplate;

	// Converts . into a mongo friendly char
	@PostConstruct
	public void setUpMongoEscapeCharacterConversion() {
	    mongoConverter.setMapKeyDotReplacement("_DOT_");
	}
	
	// ******** 3rd Party Integration Beans *****
	@Bean
	public GcpVision getGcpVision(@Value("${GCP_KEY}") String apiKey) {
		return new GcpVision(apiKey);
	}
	
	@Bean
	public ExifUtil getExifUtil() {
		return new ExifUtil();
	}
	
	
	public static void main(String[] args) {
		new HebSampleApplication().configure(new SpringApplicationBuilder(HebSampleApplication.class)).run(args);
   }
	
	

}

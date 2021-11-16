package com.l.heb.model;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ImageRepository extends MongoRepository<Image, String> {
	
	@Query("{id:'?0'}")
	Image findImageById(String id);
	
	@Query("{}")
	List<Image> findAll();
	
	List<Image> findByTagsIn(List<String> tags);
	
	public default List<Image> findByTagsAll(List<String> tags, MongoTemplate mongoTemplate) {
		List<Image> result = mongoTemplate.query(Image.class)
				.matching(query(where("tags").all(tags)))
				.all();
		return result;
	}
	

}

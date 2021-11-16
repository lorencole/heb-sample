package com.l.heb.model;

import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Document()
public class Image {
	@Id
	String id;

	String sourceUri;
	@JsonIgnore
	String image_base64;
	HashMap<String,String> metadata;
	List<String> tags;
	String label;
	
	public Image() {}

	public Image(String sourceUri, String image_base64, HashMap<String, String> metadata, List<String> orLabels,
			String label) {
		super();
		this.sourceUri = sourceUri;
		this.image_base64 = image_base64;
		this.metadata = metadata;
		this.tags = orLabels;
		this.label = label;
	}

	public String getSourceUri() {
		return sourceUri;
	}

	public void setSourceUri(String sourceUri) {
		this.sourceUri = sourceUri;
	}

	public String getImage_base64() {
		return image_base64;
	}

	public void setImage_base64(String image_base64) {
		this.image_base64 = image_base64;
	}

	public HashMap<String, String> getMetadata() {
		return metadata;
	}

	public void setMetadata(HashMap<String, String> metadata) {
		this.metadata = metadata;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> orLabels) {
		this.tags = orLabels;
	}

	public String getLabel() {
		if(Strings.isEmpty(this.label)) {
			return id;
		}
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getId() {
		return id;
	}
	
		
}

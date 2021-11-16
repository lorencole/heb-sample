package com.l.heb.integration;

import java.util.ArrayList;
import java.util.List;

public class GcpVisionResponseBody {
	
	public List<Response> responses;
	
	public class LabelAnnotation {
	    public String mid;
	    public String description;
	    public double score;
	    public double topicality;
	}

	public class Response {
	    public List<LabelAnnotation> labelAnnotations;
	}
	
	public List<String> getLabels() {
		Response response = responses.get(0);
		List<String> labels = new ArrayList<>();
		for(LabelAnnotation a : response.labelAnnotations) {
			labels.add(a.description.toLowerCase());
		}
		return labels;
	}
}

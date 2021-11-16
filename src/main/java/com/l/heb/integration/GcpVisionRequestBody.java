package com.l.heb.integration;

import java.util.ArrayList;
import java.util.List;

public class GcpVisionRequestBody {
	
	public static final String FEATURE_TYPE_LABEL_DETECTION = "LABEL_DETECTION";

    public List<Request> requests;
    
    GcpVisionRequestBody() {};
    
    GcpVisionRequestBody(Request request) {
    	this.requests = new ArrayList<>();
    	requests.add(request);
    }
    
    static GcpVisionRequestBody uriGcpVisionRequestBody(String url, String featureType, Integer maxResults) {
    	Request request = uriLabelRequest(url, featureType, maxResults);
    	GcpVisionRequestBody ret = new GcpVisionRequestBody(request);
    	return ret;
    }
    
    static GcpVisionRequestBody base64GcpVisionRequestBody(String base64Image, String featureType, Integer maxResults) {
    	Request request = base64LabelRequest(base64Image, featureType, maxResults);
    	GcpVisionRequestBody ret = new GcpVisionRequestBody(request);
    	return ret;
    }
    
    static Request uriLabelRequest(String uri, String featureType, Integer maxResults) {
    	Request request = new Request();
    	request.image = new Image();
    	request.image.source = new Source();
    	request.image.source.imageUri = uri;
    	initializeFeatures(request, featureType, maxResults);
    	return request;
    }
    
    static Request base64LabelRequest(String base64Image, String featureType, Integer maxResults) {
    	Request request = new Request();
    	request.image = new Image();
    	request.image.content = base64Image;
    	initializeFeatures(request, featureType, maxResults);
    	return request;
    }
    
    static void initializeFeatures(Request request, String featureType, Integer maxResults) {
    	request.features = new ArrayList<>();
    	request.features.add(new Feature(featureType, maxResults));
    }
    
	static public class Request{
	    public Image image;
	    public List<Feature> features;
	}
	
	static public class Source{
	    public String imageUri;
	}

	static public class Image{
	    public Source source;
	    public String content; //Base64 encoded string 
	}

	static public class Feature{
	    public String type;
	    public Integer maxResults;
	    
	    public Feature() {}
	    
		Feature(String type, Integer maxResults) {
			super();
			this.type = type;
			this.maxResults = maxResults;
		}
	}

}
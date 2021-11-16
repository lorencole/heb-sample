package com.l.heb.integration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GcpVision {

	private final OkHttpClient client = new OkHttpClient();
	private final Gson gson = new Gson();
	
	public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
	private static final String TARGET_URL = "https://vision.googleapis.com/v1/images:annotate?";
	private static final Integer MAX_RESULTS = 4;
	
	private static String API_KEY;

	public GcpVision(String apiKey) {
		API_KEY = "key=" + apiKey;
	}
	
	public List<String> tagBase64Image(String base64ImageString) {
		List<String> orLabels = new ArrayList<>();

		// Set up our label request
		GcpVisionRequestBody body = GcpVisionRequestBody.base64GcpVisionRequestBody(base64ImageString,
				GcpVisionRequestBody.FEATURE_TYPE_LABEL_DETECTION, MAX_RESULTS);

		// Execute request
		GcpVisionResponseBody gcpResponse = executeRequest(body);
		
		// parse response for labels
		orLabels = gcpResponse.getLabels();
		
		return orLabels;
	}

	public List<String> tagUriImage(String imageUrl) {

		List<String> orLabels = new ArrayList<>();

		// Set up our label request
		GcpVisionRequestBody body = GcpVisionRequestBody.uriGcpVisionRequestBody(imageUrl,
				GcpVisionRequestBody.FEATURE_TYPE_LABEL_DETECTION, MAX_RESULTS);

		// Execute request
		GcpVisionResponseBody gcpResponse = executeRequest(body);

		// parse response for labels
		orLabels = gcpResponse.getLabels();

		return orLabels;
	}
	
	private GcpVisionResponseBody executeRequest(GcpVisionRequestBody body) {
		GcpVisionResponseBody gcpResponse;
		
		Request request = new Request.Builder()
				.url(TARGET_URL + API_KEY)
				.post(RequestBody.create(JSON, gson.toJson(body)))
				.build();
		
		// any failure will throw runtime exception
		try (Response response = client.newCall(request).execute()) {
			if(response.isSuccessful()) {
				gcpResponse = gson.fromJson(response.body().string(), GcpVisionResponseBody.class);
			} else {
				throw new IOException(response.body().string());
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to execute GCP Vision Request", e);
		}
		return gcpResponse;
	}

}

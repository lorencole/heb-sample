package com.l.heb.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GcpVisionTest {
    @Autowired	// Still uses prod key, set up a separate test profile to use a test api key
    private GcpVision gcpVision;

	@Test
	void testTagImageInputStream() throws IOException {
		// load an image file 
		String path = "src/test/resources/image3.jpg";
		File file = new File(path);
		System.out.println("Testing with file: " + file.getAbsolutePath());
		byte[] fileContent = Files.readAllBytes(file.toPath());
		
		// convert to base64
		String base64Image =  Base64.getEncoder().encodeToString(fileContent);
		
		// send to gcpVision
		List<String> labels = gcpVision.tagBase64Image(base64Image);
		
		// test for labels
		assertEquals(4, labels.size());
		assertTrue(labels.stream().anyMatch(e -> e.equals("whiskers")));
		assertTrue(labels.stream().anyMatch(e -> e.equals("guinea pig")));
		assertTrue(labels.stream().anyMatch(e -> e.equals("fawn")));
	}

	@Test
	void testTagImageStringGs() {
		// Test with google storage bucket, like documenation suggests
		String uri = "gs://cloud-samples-data/vision/using_curl/shanghai.jpeg";
		
		List<String> labels = gcpVision.tagUriImage(uri);
		
		assertEquals(4, labels.size());
		assertTrue(labels.stream().anyMatch(e -> e.equals("wheel")));
		assertTrue(labels.stream().anyMatch(e -> e.equals("tire")));
		assertTrue(labels.stream().anyMatch(e -> e.equals("photograph")));
	}
	
	@Test
	void testTagImageStringurl() {
		// Test with url - woot! it works!
		String uri = "https://www.planetware.com/wpimages/2020/03/world-most-beautiful-waterfalls-iguazu-falls.jpg";
		
		List<String> labels = gcpVision.tagUriImage(uri);
		
		assertEquals(4, labels.size());
		assertTrue(labels.stream().anyMatch(e -> e.equals("rainbow")));
		assertTrue(labels.stream().anyMatch(e -> e.equals("water")));
		assertTrue(labels.stream().anyMatch(e -> e.equals("cloud")));
	}
	
}

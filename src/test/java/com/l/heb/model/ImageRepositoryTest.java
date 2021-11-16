package com.l.heb.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataMongoTest
@ExtendWith(SpringExtension.class)
class ImageRepositoryTest {
	
	@Autowired 
	ImageRepository imageRepo;
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Test
	void testFindImageById() {
		Image image = new Image();
		image.setLabel("Image1");
		imageRepo.save(image);
		
		Image persistedImage = imageRepo.findImageById(image.id);
		
		assertEquals(image.getId(), persistedImage.getId());
	}
	
	@Test
	void testFindAll() {
		Image image1 = new Image();
		image1.setLabel("Image1");
		imageRepo.save(image1);
		Image image2 = new Image();
		image2.setLabel("Image2");
		imageRepo.save(image2);
		Image image3 = new Image();
		image3.setLabel("Image3");
		imageRepo.save(image3);
		Image image4 = new Image();
		image4.setLabel("Image4");
		imageRepo.save(image4);
		
		List<Image> allImages = imageRepo.findAll();
		
		assertEquals(4, allImages.size());
		assertTrue(allImages.stream().anyMatch(e -> e.getId().equals(image1.getId())));
		assertTrue(allImages.stream().anyMatch(e -> e.getId().equals(image2.getId())));
		assertTrue(allImages.stream().anyMatch(e -> e.getId().equals(image3.getId())));
		assertTrue(allImages.stream().anyMatch(e -> e.getId().equals(image4.getId())));
	}
	
	@Test
	void testFindbyOrLabel() {
		Image image1 = new Image();
		image1.setTags(Arrays.asList("cat", "pillow", "bell"));
		imageRepo.save(image1);
		Image image2 = new Image();
		image2.setTags(Arrays.asList("cat"));
		imageRepo.save(image2);
		Image image3 = new Image();
		image3.setTags(Arrays.asList("cat", "bell", "claw"));
		imageRepo.save(image3);
		Image image4 = new Image();
		image4.setTags(Arrays.asList("cat", "claw"));
		imageRepo.save(image4);
		
		List<Image> catFilteredImages = imageRepo.findByTagsIn(Arrays.asList("cat"));
		assertEquals(4, catFilteredImages.size());
		assertTrue(catFilteredImages.stream().anyMatch(e -> e.getId().equals(image1.getId())));
		assertTrue(catFilteredImages.stream().anyMatch(e -> e.getId().equals(image2.getId())));
		assertTrue(catFilteredImages.stream().anyMatch(e -> e.getId().equals(image3.getId())));
		assertTrue(catFilteredImages.stream().anyMatch(e -> e.getId().equals(image4.getId())));
		
		List<Image> bellClawFilteredImagesOR = imageRepo.findByTagsIn(Arrays.asList("bell", "claw"));
		assertEquals(3, bellClawFilteredImagesOR.size());
		assertTrue(bellClawFilteredImagesOR.stream().anyMatch(e -> e.getId().equals(image1.getId())));
		assertTrue(bellClawFilteredImagesOR.stream().anyMatch(e -> e.getId().equals(image3.getId())));
		assertTrue(bellClawFilteredImagesOR.stream().anyMatch(e -> e.getId().equals(image4.getId())));
		
		List<Image> bellClawFilteredImagesAND = imageRepo.findByTagsAll(Arrays.asList("bell", "claw"), mongoTemplate);
		assertEquals(1, bellClawFilteredImagesAND.size());
		assertTrue(bellClawFilteredImagesAND.stream().anyMatch(e -> e.getId().equals(image3.getId())));
		
	}

	@Test
	void testSave() throws IOException {
		
		// orLabels
    	List<String> orLabels = Arrays.asList("one", "two");

    	// Metadata
    	HashMap<String, String> metadata = new HashMap<>();
    	metadata.put("[Photoshop] Seed Number","1");
    	metadata.put("[IPTC] Application Record Version","20512");
    	metadata.put("[JPEG] Compression Type","Progressive, Huffman");
    	metadata.put("[JPEG] Image Height","563 pixels");
    	metadata.put("[GPS] GPS Latitude","29° 57' 16.72\"");
    	metadata.put("[Exif SubIFD] Unique Image ID","ecc0530f4d62591e0000000000000000");
    	metadata.put("[Exif] with .","some dot data");

    	// sourceUri
    	String sourceUri = "gs://cloud-samples-data/vision/using_curl/shanghai.jpeg";
    	
    	// base64 image string
		// prepare a base64 image string
		String path = "src/test/resources/image3.jpg";
		File file = new File(path);
		System.out.println("Testing with file: " + file.getAbsolutePath());
		byte[] fileContent = Files.readAllBytes(file.toPath());
		// convert to base64
		String base64Image =  Base64.getEncoder().encodeToString(fileContent);
		
		String label = "mylabel";
    	
		
		Image image = new Image(sourceUri, base64Image, metadata, orLabels, label);
		imageRepo.save(image);
		
		Image persistedImage = imageRepo.findImageById(image.id);
		
		assertEquals("one", persistedImage.getTags().get(0));
		assertEquals("some dot data", persistedImage.getMetadata().get("[Exif] with ."));
		assertEquals("29° 57' 16.72\"", persistedImage.getMetadata().get("[GPS] GPS Latitude"));
		assertEquals(sourceUri, persistedImage.getSourceUri());
		assertEquals(base64Image, persistedImage.getImage_base64());
		assertEquals(label, persistedImage.getLabel());
	}
	
}

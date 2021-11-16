package com.l.heb.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

class ExifUtilTest {

	@Test 
	void extractMetadataFromBase64String() throws IOException {
		String path = "src/test/resources/grackles_smoking.jpg";
		File file = new File(path);
		System.out.println("Testing with file: " + file.getAbsolutePath());
		InputStream in = new BufferedInputStream(new FileInputStream(file));
		String base64ImageString = Base64.getEncoder().encodeToString(in.readAllBytes());
		in.close();
		
		ExifUtil util = new ExifUtil();
		HashMap<String, String> metadata = util.extractMetadataFromBase64String(base64ImageString);
		
		assertEquals("8 bits", metadata.get("[JPEG] Data Precision"));
		assertEquals("Baseline", metadata.get("[JPEG] Compression Type"));
	}
	
	@Test
	void testExtractMetadata() throws IOException {
		
		String path = "src/test/resources/image3.jpg";
		File file = new File(path);
		System.out.println("Testing with file: " + file.getAbsolutePath());
		InputStream in = new BufferedInputStream(new FileInputStream(file));
		ExifUtil util = new ExifUtil();
		HashMap<String, String> metadata = util.extractMetadata(in);
		
		assertEquals("1", metadata.get("[Photoshop] Seed Number"));
		assertEquals("20512", metadata.get("[IPTC] Application Record Version"));
		assertEquals("Baseline", metadata.get("[JPEG] Compression Type"));
		assertEquals("503 pixels", metadata.get("[JPEG] Image Height"));

		String path2 = "src/test/resources/image4.jpg";
		File file2 = new File(path2);
		System.out.println("Testing with file: " + file2.getAbsolutePath());
		InputStream in2 = new BufferedInputStream(new FileInputStream(file2));
		HashMap<String, String> metadata2 = util.extractMetadata(in2);
	
		assertEquals("29° 57' 16.72\"", metadata2.get("[GPS] GPS Latitude"));
		assertEquals("ecc0530f4d62591e0000000000000000", metadata2.get("[Exif SubIFD] Unique Image ID"));
		
	}

}
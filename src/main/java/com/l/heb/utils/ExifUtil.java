package com.l.heb.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.bson.internal.Base64;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

public class ExifUtil {
	
	public HashMap<String, String> extractMetadataFromBase64String(String base64ImageString) {
		InputStream imageStream;
		
		imageStream = new ByteArrayInputStream(Base64.decode(base64ImageString));
		
		return extractMetadata(imageStream);
	}
	
	public HashMap<String, String> extractMetadata(InputStream imageStream) {
		Metadata _metadata;
		HashMap<String, String> prettyPrintMetadata = new HashMap<>();
		try {
			_metadata = ImageMetadataReader.readMetadata(imageStream);
		} catch (ImageProcessingException | IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to extract metadata from image");
		}

		for (Directory directory : _metadata.getDirectories()) {
		    for (Tag tag : directory.getTags()) {
		    	String tagName = String.format("[%s] %s", directory.getName(), tag.getTagName());
		        prettyPrintMetadata.put(tagName, tag.getDescription());
		    }
		}
		
		return prettyPrintMetadata;
	}

}

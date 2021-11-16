package com.l.heb.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l.heb.integration.GcpVision;
import com.l.heb.model.Image;
import com.l.heb.model.ImageRepository;
import com.l.heb.utils.ExifUtil;

@Service
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class ImageController {
	
	@Autowired
	ImageRepository imageRepo;
	
	@Autowired
	GcpVision gcpVision;
	
	@Autowired 
	ExifUtil exifUtil;

	@GET
	@Path("/hello")
	@Produces(MediaType.TEXT_PLAIN)
	public @ResponseBody String hello() {
		return "Hello world";
	}

	@POST
	@Path("/images")
	public Image create(Request request) {
		Image image = request.toImage();
		// check for objectDetection
		if(request.detectObjects != null && request.detectObjects == true) {
			// if yes request tags from GcpVision
			if(Strings.isNotBlank(request.image_base64) ) {
				image.setTags(gcpVision.tagBase64Image(request.image_base64));
			} else if(Strings.isNotBlank(request.sourceUri)) {
				image.setTags(gcpVision.tagUriImage(request.sourceUri));
			}
		}
		// detect metadata
		if(Strings.isNotBlank(image.getImage_base64())) {
			image.setMetadata(exifUtil.extractMetadataFromBase64String(request.image_base64));
		}
		// Persist image record
		image = imageRepo.save(image);

		// return image response
		return image;
	}

	@GET
	@Path("/images")
	public List<Image> getAll(@QueryParam("objects") String objects) {
		List<Image> images;
		if(Strings.isBlank(objects)) {
			// query for all images in db
			images = imageRepo.findAll();
		} else {
			// splitterate objects to list of criteria
			List<String> criteria = Arrays.stream(StringUtils.split(objects, ","))
					.map(s -> StringUtils.trim(s.toLowerCase()))
					.collect(Collectors.toList());
			// find by criteria
			images = imageRepo.findByTagsIn(criteria);
		}
		
		// return a list of all images
		return images;
	}

	@GET
	@Path("/images/{imageId}")
	public Image getImage(@PathParam("imageId") String imageId) {
		Image image = imageRepo.findImageById(imageId);
		return image;
	}
	
	public static class Request {
		public String sourceUri;
		public String image_base64;
		public String label;
		public Boolean detectObjects = false;
		
		public Image toImage() {
			Image image = new Image();
			image.setSourceUri(sourceUri);
			image.setImage_base64(image_base64);
			image.setLabel(label);
			return image;
		}
	}
}

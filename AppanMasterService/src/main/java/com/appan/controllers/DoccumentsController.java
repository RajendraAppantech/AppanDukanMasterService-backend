package com.appan.controllers;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.utils.MyUtils;

@RestController
@RequestMapping("app")
public class DoccumentsController {
	
	@Value("${doc_token}")
	private String docToken; 
	
	@Value("${server_doc_path}")
	private String pdfPath;
	
//	@Value("${HELLO_TEST}")
//	private String textFileReader;
//	
	private static final Logger logger = LoggerFactory.getLogger(MyUtils.class);

	@GetMapping("/image/{param1}/{param2}")
    public ResponseEntity<InputStreamResource> fetchImage(@PathVariable String param1,  @PathVariable String param2, 
			 @RequestParam String id, @RequestParam String fileName) throws IOException {

        // Construct the file path
        String filePath = pdfPath + param1+"/"+param2+"/" + id + "/" + fileName;
        logger.info("textFileReader : "+filePath);
        File file = new File(filePath);

        /*if(!token.equalsIgnoreCase(docToken)) {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }*/
        
        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Create a FileSystemResource to stream the file
        FileSystemResource resource = new FileSystemResource(file);

        // Set the headers for the response
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE);  // Adjust MIME type if necessary

        // Return the file as a response
        return ResponseEntity.ok()
                             .headers(headers)
                             .body(new InputStreamResource(resource.getInputStream()));
    }
}

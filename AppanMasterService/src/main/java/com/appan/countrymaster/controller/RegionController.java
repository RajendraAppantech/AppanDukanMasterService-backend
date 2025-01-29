package com.appan.countrymaster.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.countrymaster.region.models.AuthOrBlockRegionRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.countrymaster.region.models.CreateRegionRequest;
import com.appan.countrymaster.region.models.FetchRegionRequest;
import com.appan.countrymaster.region.models.FetchRegionResponse;
import com.appan.countrymaster.region.models.ModifyRegionRequest;
import com.appan.countrymaster.services.AuthBlockRegionService;
import com.appan.countrymaster.services.CreateRegionService;
import com.appan.countrymaster.services.FetchRegionService;
import com.appan.countrymaster.services.ModifyRegionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/region")
@Validated
public class RegionController {

	@Autowired
	private CreateRegionService createService;

	@Autowired
	private FetchRegionService fetchService;

	@Autowired
	private ModifyRegionService modifyService;

	@Autowired
	private AuthBlockRegionService authBlockService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody CreateRegionRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public FetchRegionResponse fetch(@Valid @RequestBody FetchRegionRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody ModifyRegionRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody AuthOrBlockRegionRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public FetchRegionResponse getAllData() {
		return fetchService.getAllData();
	}

	@GetMapping("/image")
    public ResponseEntity<InputStreamResource> fetchImage(@RequestParam String id, @RequestParam String fileName) throws IOException {
        String pdfPath = "D:\\documents\\country\\";

        // Construct the file path
        String filePath = pdfPath + "/" + id + "/" + fileName;
        File file = new File(filePath);

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

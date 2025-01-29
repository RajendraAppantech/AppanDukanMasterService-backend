package com.appan.apimaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.apimaster.models.CreateWhatsappApiMaster;
import com.appan.apimaster.models.FetchWhatsAppApiResponse;
import com.appan.apimaster.models.FetchWhatsappApiMaster;
import com.appan.apimaster.models.ModifyWhatsappApiMaster;
import com.appan.apimaster.models.WhatsappApiMasterAuthOrBlockRequest;
import com.appan.apimaster.services.WhatsappApiMasterAuthOrBlockService;
import com.appan.apimaster.services.WhatsappApiMasterCreateService;
import com.appan.apimaster.services.WhatsappApiMasterFetchService;
import com.appan.apimaster.services.WhatsappApiMasterModifyService;
import com.appan.countrymaster.region.models.CommonResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/whatsappApi")
@Validated
public class WhatsappApiMasterController {

	@Autowired
	private WhatsappApiMasterCreateService whatsappApiMasterCreateService;

	@Autowired
	private WhatsappApiMasterFetchService whatsappApiMasterFetchService;

	@Autowired
	private WhatsappApiMasterModifyService whatsappApiMasterModifyService;

	@Autowired
	private WhatsappApiMasterAuthOrBlockService whatsappApiMasterAuthOrBlockService;

	@PostMapping("/create")
	public CommonResponse createWhatsappApi(@Valid @RequestBody CreateWhatsappApiMaster req) {
		return whatsappApiMasterCreateService.createWhatsappApi(req);
	}

	@PostMapping("/fetch")
	public FetchWhatsAppApiResponse fetchWhatsappApi(@Valid @RequestBody FetchWhatsappApiMaster req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return whatsappApiMasterFetchService.fetchWhatsappApi(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modifyWhatsappApi(@Valid @RequestBody ModifyWhatsappApiMaster req) {
		return whatsappApiMasterModifyService.modifyWhatsappApi(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorBlockWhatsappApi(@Valid @RequestBody WhatsappApiMasterAuthOrBlockRequest req) {
		return whatsappApiMasterAuthOrBlockService.authorBlockWhatsappApi(req);
	}

	@GetMapping("/allData")
	public FetchWhatsAppApiResponse getAllWhatsappApis() {
		return whatsappApiMasterFetchService.getAllWhatsappApis();
	}

}

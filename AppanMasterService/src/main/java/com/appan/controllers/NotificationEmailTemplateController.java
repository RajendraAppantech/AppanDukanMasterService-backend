package com.appan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.notification.models.EmailTemplateAuthOrBlockRequest;
import com.appan.notification.models.EmailTemplateCreateRequest;
import com.appan.notification.models.EmailTemplateFetchRequest;
import com.appan.notification.models.EmailTemplateFetchResponse;
import com.appan.notification.models.EmailTemplateModifyRequest;
import com.appan.notification.models.SendSmsRequest;
import com.appan.notification.services.EmailTemplateAuthOrBlockService;
import com.appan.notification.services.EmailTemplateCreateService;
import com.appan.notification.services.EmailTemplateFetchService;
import com.appan.notification.services.EmailTemplateModifyService;
import com.appan.notification.services.SendEmailService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/notification/emailTemp")
@Validated
public class NotificationEmailTemplateController {

	@Autowired
	private EmailTemplateCreateService createService;

	@Autowired
	private EmailTemplateFetchService fetchService;

	@Autowired
	private EmailTemplateModifyService modifyService;

	@Autowired
	private EmailTemplateAuthOrBlockService authBlockService;

	@Autowired
	private SendEmailService sendEmailService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody EmailTemplateCreateRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public EmailTemplateFetchResponse fetch(@Valid @RequestBody EmailTemplateFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody EmailTemplateModifyRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody EmailTemplateAuthOrBlockRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public EmailTemplateFetchResponse getAllData() {
		return fetchService.getAllData();
	}

	@PostMapping("/sendEmail")
	public CommonResponse sendEmail(@Valid @RequestBody SendSmsRequest req) {
		return sendEmailService.sendEmail(req);
	}

	@PostMapping("/bulkEmail")
	public CommonResponse bulkEmail(@Valid @RequestBody SendSmsRequest req) {
		return sendEmailService.bulkEmail(req);
	}
}
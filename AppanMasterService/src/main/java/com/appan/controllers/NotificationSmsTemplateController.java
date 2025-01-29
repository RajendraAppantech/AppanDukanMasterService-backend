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
import com.appan.notification.models.SendSmsRequest;
import com.appan.notification.models.SmsTemplateAuthOrBlockRequest;
import com.appan.notification.models.SmsTemplateCreateRequest;
import com.appan.notification.models.SmsTemplateFetchRequest;
import com.appan.notification.models.SmsTemplateFetchResponse;
import com.appan.notification.models.SmsTemplateModifyRequest;
import com.appan.notification.services.SendSmsService;
import com.appan.notification.services.SmsTemplateAuthOrBlockService;
import com.appan.notification.services.SmsTemplateCreateService;
import com.appan.notification.services.SmsTemplateFetchService;
import com.appan.notification.services.SmsTemplateModifyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/notification/smsTemp")
@Validated
public class NotificationSmsTemplateController {

	@Autowired
	private SmsTemplateCreateService createService;

	@Autowired
	private SmsTemplateFetchService fetchService;

	@Autowired
	private SmsTemplateModifyService modifyService;

	@Autowired
	private SmsTemplateAuthOrBlockService authBlockService;

	@Autowired
	private SendSmsService sendSmsService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody SmsTemplateCreateRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public SmsTemplateFetchResponse fetch(@Valid @RequestBody SmsTemplateFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody SmsTemplateModifyRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody SmsTemplateAuthOrBlockRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public SmsTemplateFetchResponse getAllData() {
		return fetchService.getAllData();
	}

	@PostMapping("/sendSms")
	public CommonResponse sendSms(@Valid @RequestBody SendSmsRequest req) {
		return sendSmsService.sendSms(req);
	}

	@PostMapping("/bulkSms")
	public CommonResponse bulkSms(@Valid @RequestBody SendSmsRequest req) {
		return sendSmsService.bulkSms(req);
	}
}
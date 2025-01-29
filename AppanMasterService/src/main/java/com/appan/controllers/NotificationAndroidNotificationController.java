package com.appan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.notification.models.SendAndroidRequest;
import com.appan.notification.services.SendAndroidNotificationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/notification/androidNotifi")
@Validated
public class NotificationAndroidNotificationController {

	@Autowired
	private SendAndroidNotificationService sendAndroidNotificationService;

	@PostMapping("/send")
	public CommonResponse send(@Valid @RequestBody SendAndroidRequest req) {
		return sendAndroidNotificationService.sendnotif(req);
	}

	@PostMapping("/bulk")
	public CommonResponse bulk(@Valid @RequestBody SendAndroidRequest req) {
		return sendAndroidNotificationService.bulkNotif(req);
	}

}

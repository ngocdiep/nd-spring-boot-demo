package com.nd.demo.common.service;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MessageByLocaleService {

	private MessageSource messageSource;
	
	@Autowired
	public MessageByLocaleService(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public String getMessage(String id, String... args) {
		Locale locale = LocaleContextHolder.getLocale();
		return messageSource.getMessage(id, args, locale);
	}
}
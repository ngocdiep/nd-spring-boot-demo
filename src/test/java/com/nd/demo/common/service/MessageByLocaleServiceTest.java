package com.nd.demo.common.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(value = "test")
public class MessageByLocaleServiceTest {

	@Autowired
	private MessageByLocaleService messageByLocaleService;

	@Test
	public void messageWithoutParams() {
		String message = messageByLocaleService.getMessage("test.without.params.message");
		assertEquals("Test message", message);
	}

	@Test
	public void messageWithParams() {
		String message = messageByLocaleService.getMessage("test.with.params.message", "value1", "value2");
		assertEquals("Test message, param0: value1, param1: value2", message);
	}

}

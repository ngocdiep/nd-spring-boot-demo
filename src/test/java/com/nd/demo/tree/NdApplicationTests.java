package com.nd.demo.tree;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.nd.demo.NdApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK, classes = { NdApplication.class })
@ActiveProfiles(value = "test")
public class NdApplicationTests {

	@Test
	public void contextLoads() {
	}

}

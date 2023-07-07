package com.daniel.iroribank;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

//@SpringBootTest(classes = CloudAppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@SpringBootTest
class IroriBankApplicationTests {

	@Test
	void contextLoads() {
	}

}

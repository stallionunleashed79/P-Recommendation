package com.cat.digital.reco;

import org.junit.Test
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.*

@SpringBootTest(classes = RecoApi.class)
class RecoApplicationTest extends Specification {

	@Test
	void contextLoads() {
	}

	@Test
	void applicationStartTest() {
		RecoApi.main([
				"--spring.main.web-environment=false",
				"--spring.autoconfigure.exclude=blahblahblah",
				// Override any other environment properties according to your needs
		] as String[])
	}
}

package com.cumulus.backend;

import com.cumulus.backend.user.service.EmailService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.mock;

@SpringBootTest
@ActiveProfiles("test")
class BackendApplicationTests {
	@Test
	void contextLoads() {
	}

	@TestConfiguration
	static class TestConfig {
		@Bean
		public JavaMailSender javaMailSender() {
			// Mockito mock을 직접 Bean으로 등록
			return mock(JavaMailSender.class);
		}
	}

}

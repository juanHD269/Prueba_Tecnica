package com.prueba.bikerental.testutil;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestClockConfig {

	@Bean
	@Primary
	public Clock testClock() {
		return new AdjustableClock(Instant.parse("2026-01-01T00:00:00Z"), ZoneOffset.UTC);
	}
}


package com.prueba.bikerental.testutil;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Objects;

public class AdjustableClock extends Clock {

	private final ZoneId zone;
	private Instant instant;

	public AdjustableClock(Instant initialInstant, ZoneId zone) {
		this.instant = Objects.requireNonNull(initialInstant);
		this.zone = Objects.requireNonNull(zone);
	}

	public void setInstant(Instant instant) {
		this.instant = Objects.requireNonNull(instant);
	}

	@Override
	public ZoneId getZone() {
		return zone;
	}

	@Override
	public Clock withZone(ZoneId zone) {
		return new AdjustableClock(instant, zone);
	}

	@Override
	public Instant instant() {
		return instant;
	}
}


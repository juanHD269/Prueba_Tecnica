package com.prueba.bikerental.api.rental;

import java.math.BigDecimal;
import java.time.Instant;

public record RentalResponse(
		Long id,
		String bicycleCode,
		String clientName,
		Instant startTime,
		Instant endTime,
		Integer estimatedDurationHours,
		Long actualMinutes,
		Integer billableHours,
		BigDecimal baseCost,
		BigDecimal fine,
		BigDecimal totalCost,
		boolean hasFine
) {
}


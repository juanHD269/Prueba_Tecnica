package com.prueba.bikerental.api.bicycle;

import com.prueba.bikerental.rental.Rental;
import java.math.BigDecimal;
import java.time.Instant;

public record BicycleRentalHistoryItem(
		Long rentalId,
		String clientName,
		Instant startTime,
		Instant endTime,
		Long actualMinutes,
		BigDecimal totalCost,
		boolean hasFine
) {
	public static BicycleRentalHistoryItem from(Rental rental) {
		BigDecimal totalCost = rental.getTotalCost();
		BigDecimal fine = rental.getFine();
		return new BicycleRentalHistoryItem(
				rental.getId(),
				rental.getClientName(),
				rental.getStartTime(),
				rental.getEndTime(),
				rental.getActualMinutes(),
				totalCost,
				fine != null && fine.compareTo(BigDecimal.ZERO) > 0
		);
	}
}


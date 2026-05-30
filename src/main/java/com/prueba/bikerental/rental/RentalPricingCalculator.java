package com.prueba.bikerental.rental;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Component;

@Component
public class RentalPricingCalculator {

	private static final BigDecimal HALF = new BigDecimal("0.5");

	public RentalPricing calculate(long actualMinutes, int estimatedHours, BigDecimal hourlyRate) {
		int billableHours = ceilHours(actualMinutes);
		BigDecimal baseCost = hourlyRate.multiply(BigDecimal.valueOf(billableHours));

		long delayMinutes = Math.max(0, actualMinutes - (long) estimatedHours * 60);
		int delayBillableHours = ceilHours(delayMinutes);

		BigDecimal finePerHour = hourlyRate.multiply(HALF).setScale(0, RoundingMode.HALF_UP);
		BigDecimal fine = finePerHour.multiply(BigDecimal.valueOf(delayBillableHours));

		BigDecimal total = baseCost.add(fine);

		return new RentalPricing(
				billableHours,
				baseCost,
				delayBillableHours,
				fine,
				total
		);
	}

	static int ceilHours(long minutes) {
		if (minutes <= 0) {
			return 0;
		}
		return (int) ((minutes + 59) / 60);
	}
}


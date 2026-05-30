package com.prueba.bikerental.rental;

import java.math.BigDecimal;

public record RentalPricing(
		int billableHours,
		BigDecimal baseCost,
		int delayBillableHours,
		BigDecimal fine,
		BigDecimal totalCost
) {
}


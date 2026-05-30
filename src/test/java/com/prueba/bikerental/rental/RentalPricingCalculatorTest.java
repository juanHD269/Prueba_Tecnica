package com.prueba.bikerental.rental;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class RentalPricingCalculatorTest {

	private final RentalPricingCalculator calculator = new RentalPricingCalculator();

	@Test
	void exampleFromStatement_montana_estimated2h_actual3h20m() {
		BigDecimal rate = BigDecimal.valueOf(5000);
		long actualMinutes = 3 * 60 + 20;
		int estimatedHours = 2;

		RentalPricing pricing = calculator.calculate(actualMinutes, estimatedHours, rate);

		assertEquals(4, pricing.billableHours());
		assertEquals(0, pricing.baseCost().compareTo(BigDecimal.valueOf(20000)));
		assertEquals(2, pricing.delayBillableHours());
		assertEquals(0, pricing.fine().compareTo(BigDecimal.valueOf(5000)));
		assertEquals(0, pricing.totalCost().compareTo(BigDecimal.valueOf(25000)));
	}

	@Test
	void baseCost_roundsUpToNextHour() {
		BigDecimal rate = BigDecimal.valueOf(3500);
		long actualMinutes = 1 * 60 + 10;
		int estimatedHours = 10;

		RentalPricing pricing = calculator.calculate(actualMinutes, estimatedHours, rate);

		assertEquals(2, pricing.billableHours());
		assertEquals(0, pricing.baseCost().compareTo(BigDecimal.valueOf(7000)));
		assertEquals(0, pricing.fine().compareTo(BigDecimal.ZERO));
	}

	@Test
	void fine_minimumBillableDelayIsOneHour() {
		BigDecimal rate = BigDecimal.valueOf(7500);
		long actualMinutes = 2 * 60 + 1;
		int estimatedHours = 2;

		RentalPricing pricing = calculator.calculate(actualMinutes, estimatedHours, rate);

		assertEquals(1, pricing.delayBillableHours());
		assertEquals(0, pricing.fine().compareTo(BigDecimal.valueOf(3750)));
	}
}

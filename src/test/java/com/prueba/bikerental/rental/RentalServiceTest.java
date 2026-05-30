package com.prueba.bikerental.rental;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.prueba.bikerental.bicycle.Bicycle;
import com.prueba.bikerental.bicycle.BicycleRepository;
import com.prueba.bikerental.bicycle.BicycleStatus;
import com.prueba.bikerental.bicycle.BicycleType;
import com.prueba.bikerental.common.ConflictException;
import com.prueba.bikerental.testutil.AdjustableClock;
import com.prueba.bikerental.testutil.TestClockConfig;

@SpringBootTest
@Import(TestClockConfig.class)
class RentalServiceTest {

	private final BicycleRepository bicycleRepository;
	private final RentalRepository rentalRepository;
	private final RentalService rentalService;
	private final Clock clock;

	@Autowired
	RentalServiceTest(BicycleRepository bicycleRepository, RentalRepository rentalRepository, RentalService rentalService, Clock clock) {
		this.bicycleRepository = bicycleRepository;
		this.rentalRepository = rentalRepository;
		this.rentalService = rentalService;
		this.clock = clock;
	}

	@BeforeEach
	void setUp() {
		rentalRepository.deleteAll();
		bicycleRepository.deleteAll();
	}

	@Test
	void startAndFinishRental_updatesBicycleStatus_andComputesCosts() {
		Bicycle bicycle = bicycleRepository.save(new Bicycle("BIC-T1", BicycleType.MONTANA, BicycleStatus.DISPONIBLE));

		AdjustableClock adjustableClock = (AdjustableClock) clock;
		adjustableClock.setInstant(Instant.parse("2026-01-01T10:00:00Z"));
		Rental rental = rentalService.startRental(bicycle.getCode(), "Juan", 2);

		assertNotNull(rental.getId());
		assertEquals(BicycleStatus.ALQUILADA, bicycleRepository.findByCode("BIC-T1").orElseThrow().getStatus());

		adjustableClock.setInstant(Instant.parse("2026-01-01T13:20:00Z"));
		Rental finished = rentalService.finishRental(rental.getId());

		assertNotNull(finished.getEndTime());
		assertEquals(BicycleStatus.DISPONIBLE, bicycleRepository.findByCode("BIC-T1").orElseThrow().getStatus());

		assertEquals(200L, finished.getActualMinutes());
		assertEquals(4, finished.getBillableHours());
		assertEquals(0, finished.getBaseCost().compareTo(BigDecimal.valueOf(20000)));
		assertEquals(0, finished.getFine().compareTo(BigDecimal.valueOf(5000)));
		assertEquals(0, finished.getTotalCost().compareTo(BigDecimal.valueOf(25000)));
	}

	@Test
	void startRental_rejectsNonAvailableBicycle() {
		bicycleRepository.save(new Bicycle("BIC-T2", BicycleType.URBANA, BicycleStatus.EN_MANTENIMIENTO));
		ConflictException ex = assertThrows(ConflictException.class, () -> rentalService.startRental("BIC-T2", "Juan", 1));
		assertNotNull(ex.getMessage());
	}
}

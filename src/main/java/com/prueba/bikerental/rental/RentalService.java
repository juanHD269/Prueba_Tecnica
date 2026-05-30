package com.prueba.bikerental.rental;

import com.prueba.bikerental.bicycle.Bicycle;
import com.prueba.bikerental.bicycle.BicycleStatus;
import com.prueba.bikerental.bicycle.BicycleType;
import com.prueba.bikerental.bicycle.BicycleRepository;
import com.prueba.bikerental.common.ConflictException;
import com.prueba.bikerental.common.NotFoundException;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RentalService {

	private final Clock clock;
	private final BicycleRepository bicycleRepository;
	private final RentalRepository rentalRepository;
	private final RentalPricingCalculator pricingCalculator;

	public RentalService(
			Clock clock,
			BicycleRepository bicycleRepository,
			RentalRepository rentalRepository,
			RentalPricingCalculator pricingCalculator
	) {
		this.clock = clock;
		this.bicycleRepository = bicycleRepository;
		this.rentalRepository = rentalRepository;
		this.pricingCalculator = pricingCalculator;
	}

	@Transactional
	public Rental startRental(String bicycleCode, String clientName, int estimatedDurationHours) {
		Bicycle bicycle = bicycleRepository.findByCode(bicycleCode)
				.orElseThrow(() -> new NotFoundException("No existe bicicleta con código " + bicycleCode));

		if (bicycle.getStatus() != BicycleStatus.DISPONIBLE) {
			throw new ConflictException("La bicicleta " + bicycleCode + " no está disponible para alquiler (estado: " + bicycle.getStatus() + ")");
		}

		bicycle.setStatus(BicycleStatus.ALQUILADA);

		Instant startTime = clock.instant();
		Rental rental = new Rental(bicycle, clientName, startTime, estimatedDurationHours);
		return rentalRepository.save(rental);
	}

	@Transactional
	public Rental finishRental(Long rentalId) {
		Rental rental = rentalRepository.findByIdWithBicycle(rentalId)
				.orElseThrow(() -> new NotFoundException("No existe alquiler con id " + rentalId));

		if (rental.isFinished()) {
			throw new ConflictException("El alquiler " + rentalId + " ya fue finalizado");
		}

		Instant endTime = clock.instant();
		long actualMinutes = Math.max(0, Duration.between(rental.getStartTime(), endTime).toMinutes());

		BigDecimal hourlyRate = hourlyRateFor(rental.getBicycle().getType());
		RentalPricing pricing = pricingCalculator.calculate(actualMinutes, rental.getEstimatedDurationHours(), hourlyRate);

		rental.finish(endTime, actualMinutes, pricing);
		rental.getBicycle().setStatus(BicycleStatus.DISPONIBLE);

		return rental;
	}

	@Transactional(readOnly = true)
	public List<Rental> getHistoryByBicycleCode(String bicycleCode) {
		if (!bicycleRepository.findByCode(bicycleCode).isPresent()) {
			throw new NotFoundException("No existe bicicleta con código " + bicycleCode);
		}
		return rentalRepository.findByBicycle_CodeOrderByStartTimeDesc(bicycleCode);
	}

	private static BigDecimal hourlyRateFor(BicycleType type) {
		return switch (type) {
			case URBANA -> BigDecimal.valueOf(3500);
			case MONTANA -> BigDecimal.valueOf(5000);
			case ELECTRICA -> BigDecimal.valueOf(7500);
		};
	}
}


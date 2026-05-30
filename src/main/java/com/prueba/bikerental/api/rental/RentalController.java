package com.prueba.bikerental.api.rental;

import com.prueba.bikerental.rental.Rental;
import com.prueba.bikerental.rental.RentalService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

	private final RentalService rentalService;

	public RentalController(RentalService rentalService) {
		this.rentalService = rentalService;
	}

	@PostMapping
	public ResponseEntity<RentalResponse> start(@Valid @RequestBody StartRentalRequest request) {
		Rental rental = rentalService.startRental(
				request.bicycleCode(),
				request.clientName(),
				request.estimatedDurationHours()
		);

		return ResponseEntity.created(URI.create("/api/rentals/" + rental.getId()))
				.body(toResponse(rental));
	}

	@PostMapping("/{id}/finish")
	public RentalResponse finish(@PathVariable("id") Long id) {
		Rental rental = rentalService.finishRental(id);
		return toResponse(rental);
	}

	private RentalResponse toResponse(Rental rental) {
		BigDecimal fine = rental.getFine();
		return new RentalResponse(
				rental.getId(),
				rental.getBicycle().getCode(),
				rental.getClientName(),
				rental.getStartTime(),
				rental.getEndTime(),
				rental.getEstimatedDurationHours(),
				rental.getActualMinutes(),
				rental.getBillableHours(),
				rental.getBaseCost(),
				fine,
				rental.getTotalCost(),
				fine != null && fine.compareTo(BigDecimal.ZERO) > 0
		);
	}
}


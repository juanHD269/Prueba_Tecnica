package com.prueba.bikerental.api.bicycle;

import com.prueba.bikerental.bicycle.Bicycle;
import com.prueba.bikerental.bicycle.BicycleService;
import com.prueba.bikerental.bicycle.BicycleType;
import com.prueba.bikerental.rental.Rental;
import com.prueba.bikerental.rental.RentalService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bicycles")
public class BicycleController {

	private final BicycleService bicycleService;
	private final RentalService rentalService;

	public BicycleController(BicycleService bicycleService, RentalService rentalService) {
		this.bicycleService = bicycleService;
		this.rentalService = rentalService;
	}

	@PostMapping
	public ResponseEntity<BicycleResponse> create(@Valid @RequestBody CreateBicycleRequest request) {
		Bicycle created = bicycleService.create(request.code(), request.type(), request.status());
		return ResponseEntity.created(URI.create("/api/bicycles/" + created.getCode()))
				.body(toResponse(created));
	}

	@GetMapping("/available")
	public List<BicycleResponse> getAvailable(@RequestParam(name = "type", required = false) BicycleType type) {
		return bicycleService.getAvailable(Optional.ofNullable(type))
				.stream()
				.map(this::toResponse)
				.toList();
	}

	@GetMapping("/{code}/rentals")
	public List<BicycleRentalHistoryItem> getHistory(@PathVariable("code") String code) {
		List<Rental> rentals = rentalService.getHistoryByBicycleCode(code);
		return rentals.stream().map(BicycleRentalHistoryItem::from).toList();
	}

	private BicycleResponse toResponse(Bicycle bicycle) {
		return new BicycleResponse(bicycle.getCode(), bicycle.getType(), bicycle.getStatus());
	}
}


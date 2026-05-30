package com.prueba.bikerental.api.rental;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record StartRentalRequest(
		@NotBlank(message = "bicycleCode es obligatorio")
		String bicycleCode,
		@NotBlank(message = "clientName es obligatorio")
		String clientName,
		@Min(value = 1, message = "estimatedDurationHours debe ser >= 1")
		int estimatedDurationHours
) {
}


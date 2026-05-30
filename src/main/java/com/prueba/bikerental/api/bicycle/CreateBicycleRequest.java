package com.prueba.bikerental.api.bicycle;

import com.prueba.bikerental.bicycle.BicycleStatus;
import com.prueba.bikerental.bicycle.BicycleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateBicycleRequest(
		@NotBlank(message = "code es obligatorio")
		String code,
		@NotNull(message = "type es obligatorio")
		BicycleType type,
		BicycleStatus status
) {
}


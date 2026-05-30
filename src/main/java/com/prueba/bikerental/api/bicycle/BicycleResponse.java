package com.prueba.bikerental.api.bicycle;

import com.prueba.bikerental.bicycle.BicycleStatus;
import com.prueba.bikerental.bicycle.BicycleType;

public record BicycleResponse(
		String code,
		BicycleType type,
		BicycleStatus status
) {
}


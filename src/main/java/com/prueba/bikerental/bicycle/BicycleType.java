package com.prueba.bikerental.bicycle;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.text.Normalizer;
import java.util.Locale;

public enum BicycleType {
	URBANA("URBANA"),
	MONTANA("MONTAÑA"),
	ELECTRICA("ELÉCTRICA");

	private final String jsonValue;

	BicycleType(String jsonValue) {
		this.jsonValue = jsonValue;
	}

	@JsonCreator
	public static BicycleType fromJson(String value) {
		if (value == null) {
			return null;
		}

		String normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
				.replaceAll("\\p{M}", "")
				.replace(' ', '_')
				.toUpperCase(Locale.ROOT);

		return switch (normalized) {
			case "URBANA" -> URBANA;
			case "MONTANA", "MONTANA_" -> MONTANA;
			case "ELECTRICA", "ELECTRICA_" -> ELECTRICA;
			default -> BicycleType.valueOf(normalized);
		};
	}

	@JsonValue
	public String toJson() {
		return jsonValue;
	}
}


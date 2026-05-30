package com.prueba.bikerental.bicycle;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
		name = "bicycles",
		uniqueConstraints = @UniqueConstraint(name = "uk_bicycle_code", columnNames = "code")
)
public class Bicycle {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, updatable = false, length = 50)
	private String code;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private BicycleType type;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private BicycleStatus status;

	protected Bicycle() {
	}

	public Bicycle(String code, BicycleType type, BicycleStatus status) {
		this.code = code;
		this.type = type;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public BicycleType getType() {
		return type;
	}

	public BicycleStatus getStatus() {
		return status;
	}

	public void setStatus(BicycleStatus status) {
		this.status = status;
	}
}


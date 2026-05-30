package com.prueba.bikerental.rental;

import com.prueba.bikerental.bicycle.Bicycle;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "rentals")
public class Rental {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "bicycle_id", nullable = false)
	private Bicycle bicycle;

	@Column(nullable = false, length = 200)
	private String clientName;

	@Column(nullable = false)
	private Instant startTime;

	@Column(nullable = false)
	private int estimatedDurationHours;

	private Instant endTime;

	private Long actualMinutes;

	private Integer billableHours;

	@Column(precision = 19, scale = 0)
	private BigDecimal baseCost;

	private Integer delayBillableHours;

	@Column(precision = 19, scale = 0)
	private BigDecimal fine;

	@Column(precision = 19, scale = 0)
	private BigDecimal totalCost;

	protected Rental() {
	}

	public Rental(Bicycle bicycle, String clientName, Instant startTime, int estimatedDurationHours) {
		this.bicycle = bicycle;
		this.clientName = clientName;
		this.startTime = startTime;
		this.estimatedDurationHours = estimatedDurationHours;
	}

	public Long getId() {
		return id;
	}

	public Bicycle getBicycle() {
		return bicycle;
	}

	public String getClientName() {
		return clientName;
	}

	public Instant getStartTime() {
		return startTime;
	}

	public int getEstimatedDurationHours() {
		return estimatedDurationHours;
	}

	public Instant getEndTime() {
		return endTime;
	}

	public Long getActualMinutes() {
		return actualMinutes;
	}

	public Integer getBillableHours() {
		return billableHours;
	}

	public BigDecimal getBaseCost() {
		return baseCost;
	}

	public Integer getDelayBillableHours() {
		return delayBillableHours;
	}

	public BigDecimal getFine() {
		return fine;
	}

	public BigDecimal getTotalCost() {
		return totalCost;
	}

	public boolean isFinished() {
		return endTime != null;
	}

	public void finish(Instant endTime, long actualMinutes, RentalPricing pricing) {
		this.endTime = endTime;
		this.actualMinutes = actualMinutes;
		this.billableHours = pricing.billableHours();
		this.baseCost = pricing.baseCost();
		this.delayBillableHours = pricing.delayBillableHours();
		this.fine = pricing.fine();
		this.totalCost = pricing.totalCost();
	}
}


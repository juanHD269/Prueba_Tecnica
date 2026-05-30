package com.prueba.bikerental.rental;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RentalRepository extends JpaRepository<Rental, Long> {

	@Query("select r from Rental r join fetch r.bicycle b where r.id = :id")
	Optional<Rental> findByIdWithBicycle(@Param("id") Long id);

	List<Rental> findByBicycle_CodeOrderByStartTimeDesc(String bicycleCode);
}

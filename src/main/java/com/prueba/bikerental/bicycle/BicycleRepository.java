package com.prueba.bikerental.bicycle;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BicycleRepository extends JpaRepository<Bicycle, Long> {
	Optional<Bicycle> findByCode(String code);

	List<Bicycle> findByStatus(BicycleStatus status);

	List<Bicycle> findByStatusAndType(BicycleStatus status, BicycleType type);
}


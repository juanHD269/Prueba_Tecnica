package com.prueba.bikerental.bicycle;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prueba.bikerental.common.ConflictException;
import com.prueba.bikerental.common.NotFoundException;

@Service
public class BicycleService {

	private final BicycleRepository bicycleRepository;

	public BicycleService(BicycleRepository bicycleRepository) {
		this.bicycleRepository = bicycleRepository;
	}

	@Transactional
	public Bicycle create(String code, BicycleType type, BicycleStatus status) {
		BicycleStatus finalStatus = status != null ? status : BicycleStatus.DISPONIBLE;
		try {
			return bicycleRepository.save(new Bicycle(code, type, finalStatus));
		} catch (DataIntegrityViolationException ex) {
			throw new ConflictException("Ya existe una bicicleta con código " + code);
		}
	}

	@Transactional(readOnly = true)
	public Bicycle getByCode(String code) {
		return bicycleRepository.findByCode(code)
				.orElseThrow(() -> new NotFoundException("No existe bicicleta con código " + code));
	}

	@Transactional(readOnly = true)
	public List<Bicycle> getAvailable(Optional<BicycleType> type) {
		if (type.isPresent()) {
			return bicycleRepository.findByStatusAndType(BicycleStatus.DISPONIBLE, type.get());
		}
		return bicycleRepository.findByStatus(BicycleStatus.DISPONIBLE);
	}
}

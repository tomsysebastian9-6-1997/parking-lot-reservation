package com.valcare.parking.service;

import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.valcare.parking.dto.ParkingFloorDto;
import com.valcare.parking.entity.ParkingFloorEntity;
import com.valcare.parking.repo.ParkingFloorRepository;
import com.valcare.parking.security.exception.ResourceValidationException;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Service
public class ParkingFloorService {

	@Autowired
	private ParkingFloorRepository parkingFloorRepository;
	@Autowired
	private Validator validator;

	/*
	 * -> input object is not null. 
	 * -> check fields are valid. 
	 * -> check all slot is valid. 
	 * -> Save object to the database atomically.
	 */
	@Transactional
	public ParkingFloorEntity createParkingFloor(ParkingFloorDto parkingFloorDto) {
		if (parkingFloorDto == null) {
			throw new ResourceValidationException("Parking floor data cannot be null");
		}

		// Validate the floor entity
		Set<ConstraintViolation<ParkingFloorDto>> violations = validator.validate(parkingFloorDto);
		if (!violations.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (ConstraintViolation<ParkingFloorDto> v : violations) {
				sb.append(v.getMessage()).append("; ");
			}
			throw new ResourceValidationException("Validation failed: " + sb.toString());
		}

		if (parkingFloorRepository.findByName(parkingFloorDto.getName()) != null)
			throw new ResourceValidationException(
					"Parking floor with name '" + parkingFloorDto.getName() + "' already exists");

		return parkingFloorRepository.save(converDtoToEntity(parkingFloorDto));
	}

	/*
	 * -> convert dto to entity.
	 */
	@Transactional
	private ParkingFloorEntity converDtoToEntity(ParkingFloorDto pfd) {
		ParkingFloorEntity pfe = new ParkingFloorEntity();
		pfe.setName(pfd.getName());
		pfe.setCreatedTimeStamp(new Date());

		return pfe;
	}
}

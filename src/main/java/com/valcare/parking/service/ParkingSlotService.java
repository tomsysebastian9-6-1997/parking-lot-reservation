package com.valcare.parking.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.valcare.parking.dto.ParkingSlotDto;
import com.valcare.parking.entity.ParkingFloorEntity;
import com.valcare.parking.entity.ParkingSlotEntity;
import com.valcare.parking.repo.ParkingFloorRepository;
import com.valcare.parking.repo.ParkingSlotRepository;
import com.valcare.parking.security.exception.ResourceValidationException;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ParkingSlotService {

	@Autowired
	private ParkingSlotRepository parkingSlotRepository;
	@Autowired
	private ParkingFloorRepository parkingFloorRepository;
	@Autowired
	private Validator validator;

	/*
	 * -> input object is not null. 
	 * -> check fields are valid. 
	 * -> set default value true in available. 
	 * -> Save object to the database atomically.
	 */
	@Transactional
	public ParkingSlotEntity createSlot(ParkingSlotDto slot) {
		if (slot == null) {
			throw new ResourceValidationException("Parking slot cannot be null");
		}

		// Validate the ParkingSlotEntity
		Set<ConstraintViolation<ParkingSlotDto>> violations = validator.validate(slot);
		if (!violations.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (ConstraintViolation<ParkingSlotDto> v : violations) {
				sb.append(v.getMessage()).append("; ");
			}
			throw new ResourceValidationException("Validation failed: " + sb.toString());
		}

		return parkingSlotRepository.save(mapDtoToEntity(slot));
	}
	
	/*
	 * -> validate floor and slot.
	 * -> convert dto to entity.
	 */
	@Transactional
	private ParkingSlotEntity mapDtoToEntity(ParkingSlotDto psd) {
		ParkingFloorEntity floor = parkingFloorRepository.findByName(psd.getFloorName());
		if (floor == null)
			throw new ResourceValidationException("Floor with name '" + psd.getFloorName() + "' does not exist");

		if (parkingSlotRepository.findBySlotNumberAndFloor(psd.getSlotNumber(), floor) != null)
			throw new ResourceValidationException("Slot with number '" + psd.getSlotNumber()
					+ "' already exists on floor '" + floor.getName() + "'");

		ParkingSlotEntity pse = new ParkingSlotEntity();
		pse.setSlotNumber(psd.getSlotNumber());
		pse.setVehicleType(psd.getVehicleType());
		pse.setFloor(floor);
		pse.setAvailable(true);

		return pse;
	}
}

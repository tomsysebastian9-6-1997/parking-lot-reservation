package com.valcare.parking.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.valcare.parking.entity.ParkingFloorEntity;
import com.valcare.parking.entity.ParkingSlotEntity;
import com.valcare.parking.entity.ReservationEntity;
import com.valcare.parking.model.ReservationRequest;
import com.valcare.parking.model.SlotAvailabilityRequest;
import com.valcare.parking.repo.ParkingFloorRepository;
import com.valcare.parking.repo.ParkingSlotRepository;
import com.valcare.parking.repo.ReservationRepository;
import com.valcare.parking.security.exception.ResourceValidationException;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Service
public class ReservationService {

	@Autowired
	private ReservationRepository reservationRepository;
	@Autowired
	private ParkingSlotRepository parkingSlotRepository;
	@Autowired
	private ParkingFloorRepository parkingFloorRepository;
	@Autowired
	private Validator validator;

	/*
	 * -> input object is not null. 
	 * -> check all fields are valid. 
	 * -> Start time must be before end time. 
	 * -> Reservation duration cannot exceed 24 hours. 
	 * -> Slot already reserved in this time range
	 * -> check valid floor or slot.
	 * -> Partial hours should be charged as a full hour. 
	 * -> Save object to the database atomically.
	 */
	@Transactional
	public ReservationEntity reserveSlot(ReservationRequest request) {
		if (request == null) {
			throw new ResourceValidationException("Reservation request cannot be null");
		}

		// Validate the request object
		Set<ConstraintViolation<ReservationRequest>> violations = validator.validate(request);
		if (!violations.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (ConstraintViolation<ReservationRequest> v : violations) {
				sb.append(v.getMessage()).append("; ");
			}
			throw new ResourceValidationException("Validation failed: " + sb.toString());
		}

		if (!request.getStartTime().isBefore(request.getEndTime()))
			throw new ResourceValidationException("Start time must be before end time");
		if (Duration.between(request.getStartTime(), request.getEndTime()).toHours() > 24)
			throw new ResourceValidationException("Cannot exceed 24 hours");
		ParkingFloorEntity pfe = parkingFloorRepository.findByName(request.getFloorName());
		if (pfe == null)
			throw new ResourceValidationException("Floor with number '" + request.getFloorName() + "' does not exist");
		ParkingSlotEntity slot = parkingSlotRepository.findBySlotNumberAndFloor(request.getSlotNumber(), pfe);
		if (slot == null)
			throw new ResourceValidationException("Slot with number '" + request.getSlotNumber()
					+ "' not exists in floor '" + request.getFloorName() + "'");
		List<ReservationEntity> conflicts = reservationRepository.findActiveReservationsForSlot(slot.getId(),
				request.getStartTime(), request.getEndTime());
		if (!conflicts.isEmpty())
			throw new ResourceValidationException("Slot already reserved in this time range");
		long hours = (long) Math
				.ceil(Duration.between(request.getStartTime(), request.getEndTime()).toMinutes() / 60.0);
		double cost = hours * slot.getVehicleType().getRatePerHour();

		return createReservation(request, slot, cost);
	}

	/* 
	 * -> convert dto to entity 
	 */
	@Transactional
	public ReservationEntity createReservation(ReservationRequest request, ParkingSlotEntity pse, Double cost) {
		ReservationEntity reservation = new ReservationEntity();
		reservation.setSlot(pse);
		reservation.setVehicleNumber(request.getVehicleNumber());
		reservation.setStartTime(request.getStartTime());
		reservation.setEndTime(request.getEndTime());
		reservation.setCost(cost);

		reservationRepository.save(reservation);
		return reservation;
	}

	/*
	 * -> input object is not null. 
	 * -> check all fields are valid. 
	 * -> Start time must be before end time. 
	 * -> find data by page number and size. 
	 */
	@Transactional
	public List<ParkingSlotEntity> getAvailableSlots(SlotAvailabilityRequest request) {
		if (request == null) {
			throw new ResourceValidationException("Availability request cannot be null");
		}

		// Validate the request object
		Set<ConstraintViolation<SlotAvailabilityRequest>> violations = validator.validate(request);
		if (!violations.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (ConstraintViolation<SlotAvailabilityRequest> v : violations) {
				sb.append(v.getMessage()).append("; ");
			}
			throw new ResourceValidationException("Validation failed: " + sb.toString());
		}

		if (!request.getStartTime().isBefore(request.getEndTime()))
			throw new ResourceValidationException("Start time must be before end time");

		Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
		Page<ParkingSlotEntity> allSlots = parkingSlotRepository.findAll(pageable);

		return allSlots.stream()
				.filter(slot -> isSlotAvailable(slot.getId(), request.getStartTime(), request.getEndTime())).toList();
	}
	
	/* 
	 * -> find active reservation slots 
	 */
	@Transactional
	public boolean isSlotAvailable(Long slotId, LocalDateTime start, LocalDateTime end) {
		List<ReservationEntity> conflicts = reservationRepository.findActiveReservationsForSlot(slotId, start, end);

		return conflicts.isEmpty();
	}

	/* 
	 * -> find active reservation slots by id
	 */
	@Transactional
	public ReservationEntity getReservationById(Long id) {
		return reservationRepository.findById(id)
				.orElseThrow(() -> new ResourceValidationException("Reservation not found"));
	}

	/* 
	 * -> remove active reservation slots by id
	 */
	@Transactional
	public void cancelReservationById(Long id) {
		ReservationEntity reservation = reservationRepository.findById(id)
				.orElseThrow(() -> new ResourceValidationException("Reservation not found with id " + id));

		reservationRepository.delete(reservation);
	}

}

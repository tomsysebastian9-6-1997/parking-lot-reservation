package com.valcare.parking.rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.valcare.parking.dto.ParkingFloorDto;
import com.valcare.parking.dto.ParkingSlotDto;
import com.valcare.parking.entity.ParkingFloorEntity;
import com.valcare.parking.entity.ParkingSlotEntity;
import com.valcare.parking.entity.ReservationEntity;
import com.valcare.parking.model.ReservationRequest;
import com.valcare.parking.model.SlotAvailabilityRequest;
import com.valcare.parking.service.ParkingFloorService;
import com.valcare.parking.service.ParkingSlotService;
import com.valcare.parking.service.ReservationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ParkingRestController {
	@Autowired
	private ParkingFloorService parkingFloorService;
	@Autowired
	private ParkingSlotService parkingSlotService;
	@Autowired
	private ReservationService reservationService;

	@PostMapping("/floors")
	public ResponseEntity<Map<String, Object>> createParkingFloor(@Valid @RequestBody ParkingFloorDto floor) {
		ParkingFloorEntity pfe = parkingFloorService.createParkingFloor(floor);

		return ResponseEntity.ok(Map.of("res", pfe));
	}

	@PostMapping("/slots")
	public ResponseEntity<Map<String, Object>> createSlot(@Valid @RequestBody ParkingSlotDto slot) {
		ParkingSlotEntity pse = parkingSlotService.createSlot(slot);

		return ResponseEntity.ok(Map.of("res", pse));
	}

	@PostMapping("/reserve")
	public ResponseEntity<Map<String, Object>> reserveSlot(@Valid @RequestBody ReservationRequest request) {
		ReservationEntity re = reservationService.reserveSlot(request);

		return ResponseEntity.ok(Map.of("res", re));
	}

	@PostMapping("/availability")
	public ResponseEntity<Map<String, Object>> getAvailableSlots( @RequestBody SlotAvailabilityRequest request) {
		List<ParkingSlotEntity> allSlots = reservationService.getAvailableSlots(request);

		return ResponseEntity.ok(Map.of("res", allSlots));
	}

	@GetMapping("/reservations/{id}")
	public ResponseEntity<Map<String, Object>> getReservation(@Valid @PathVariable Long id) {
		ReservationEntity re = reservationService.getReservationById(id);

		return ResponseEntity.ok(Map.of("res", re));
	}

	@DeleteMapping("/reservations/{id}")
	public ResponseEntity<Map<String, Object>> cancelReservation(@Valid @PathVariable Long id) {
		reservationService.cancelReservationById(id);

		return ResponseEntity.ok(Map.of("res", "Reservation Cancelled Successfully"));
	}
}

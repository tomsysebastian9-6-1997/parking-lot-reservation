package com.valcare.parking.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.valcare.parking.dto.ParkingFloorDto;
import com.valcare.parking.dto.ParkingSlotDto;
import com.valcare.parking.entity.ParkingFloorEntity;
import com.valcare.parking.entity.ParkingSlotEntity;
import com.valcare.parking.entity.ReservationEntity;
import com.valcare.parking.model.ReservationRequest;
import com.valcare.parking.model.SlotAvailabilityRequest;
import com.valcare.parking.security.exception.ResourceValidationException;
import com.valcare.parking.service.ParkingFloorService;
import com.valcare.parking.service.ParkingSlotService;
import com.valcare.parking.service.ReservationService;

public class ParkingRestControllerTest {
	@Mock
	private ParkingFloorService parkingFloorService;

	@Mock
	private ParkingSlotService parkingSlotService;

	@Mock
	private ReservationService reservationService;

	@InjectMocks
	private ParkingRestController controller;

//	private Validator validator;

	@BeforeEach // This runs before each test
	void setUp() {
		MockitoAnnotations.openMocks(this);
//		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//		validator = factory.getValidator();
	}

	@Test
	void testCreateParkingFloor_IsNull() {
		ParkingFloorDto floor = null;
		when(parkingFloorService.createParkingFloor(floor))
				.thenThrow(new ResourceValidationException("Parking floor data cannot be null"));

		ResourceValidationException exception = assertThrows(ResourceValidationException.class,
				() -> controller.createParkingFloor(floor));

		assertEquals("Parking floor data cannot be null", exception.getMessage());
		verify(parkingFloorService, times(1)).createParkingFloor(floor);
	}

	@Test
	void testCreateParkingFloor_NameIsNull() {
		ParkingFloorDto floor = new ParkingFloorDto();
		floor.setName(null);
		when(parkingFloorService.createParkingFloor(floor))
				.thenThrow(new ResourceValidationException("Floor name cannot be blank"));

		ResourceValidationException exception = assertThrows(ResourceValidationException.class,
				() -> controller.createParkingFloor(floor));

		assertEquals("Floor name cannot be blank", exception.getMessage());
		verify(parkingFloorService, times(1)).createParkingFloor(floor);
	}

	@Test
	void testCreateParkingFloor_NameIsEmpty() {
		ParkingFloorDto floor = new ParkingFloorDto();
		floor.setName("");
		when(parkingFloorService.createParkingFloor(floor))
				.thenThrow(new ResourceValidationException("Floor name cannot be blank"));

		ResourceValidationException exception = assertThrows(ResourceValidationException.class,
				() -> controller.createParkingFloor(floor));

		assertEquals("Floor name cannot be blank", exception.getMessage());
		verify(parkingFloorService, times(1)).createParkingFloor(floor);
	}

	@Test
	void testCreateParkingFloor_NameIsValid() {
		ParkingFloorDto floor = new ParkingFloorDto();
		floor.setName("1st Floor");

		ParkingFloorEntity pfe = new ParkingFloorEntity();
		pfe.setId(1L);
		pfe.setName("1st Floor");

		when(parkingFloorService.createParkingFloor(floor)).thenReturn(pfe);

		ResponseEntity<Map<String, Object>> response = controller.createParkingFloor(floor);

		assertNotNull(response);
		assertEquals(pfe, response.getBody().get("res"));

		verify(parkingFloorService, times(1)).createParkingFloor(floor);
	}

	@Test
	void testCreateSlot_IsNull() {
		ParkingSlotDto slot = null;
		when(parkingSlotService.createSlot(slot))
				.thenThrow(new ResourceValidationException("Parking slot cannot be null"));

		ResourceValidationException exception = assertThrows(ResourceValidationException.class,
				() -> controller.createSlot(slot));

		assertEquals("Parking slot cannot be null", exception.getMessage());
		verify(parkingSlotService, times(1)).createSlot(slot);
	}

	@Test
	void testCreateSlot_SlotNumberIsEmpty() {
		ParkingSlotDto psd = new ParkingSlotDto();
		psd.setSlotNumber("");
		when(parkingSlotService.createSlot(psd))
				.thenThrow(new ResourceValidationException("Slot number cannot be blank"));
		ResourceValidationException e = assertThrows(ResourceValidationException.class,
				() -> controller.createSlot(psd));
		assertEquals("Slot number cannot be blank", e.getMessage());
		verify(parkingSlotService, times(1)).createSlot(psd);
	}

	@Test
	void testCreateSlot_SlotNumberIsNull() {
		ParkingSlotDto psd = new ParkingSlotDto();
		psd.setSlotNumber(null);
		when(parkingSlotService.createSlot(psd))
				.thenThrow(new ResourceValidationException("Slot number cannot be blank"));
		ResourceValidationException e = assertThrows(ResourceValidationException.class,
				() -> controller.createSlot(psd));
		assertEquals("Slot number cannot be blank", e.getMessage());
		verify(parkingSlotService, times(1)).createSlot(psd);
	}

	@Test
	void testCreateSlot() {
		ParkingSlotDto slot = new ParkingSlotDto();
		slot.setSlotNumber("A001");
		slot.setFloorName("1st Floor");

		ParkingSlotEntity pse = new ParkingSlotEntity();
		pse.setSlotNumber("A001");
		pse.setAvailable(true);
		when(parkingSlotService.createSlot(slot)).thenReturn(pse);

		ResponseEntity<Map<String, Object>> response = controller.createSlot(slot);

		assertEquals(pse, response.getBody().get("res"));
		verify(parkingSlotService, times(1)).createSlot(slot);
	}

	@Test
	void testReserveSlot_ValidRequest() {
		ReservationRequest request = new ReservationRequest();
		request.setFloorName("1");
		request.setSlotNumber("A1");
		request.setVehicleNumber("KA01AB1234");
		request.setStartTime(LocalDateTime.now().plusHours(1));
		request.setEndTime(LocalDateTime.now().plusHours(2));

		ReservationEntity reservation = new ReservationEntity();
		when(reservationService.reserveSlot(request)).thenReturn(reservation);

		ResponseEntity<Map<String, Object>> response = controller.reserveSlot(request);

		assertEquals(reservation, response.getBody().get("res"));
		verify(reservationService, times(1)).reserveSlot(request);
	}

	@Test
	void testReserveSlot_FloorNameNull() {
		ReservationRequest request = new ReservationRequest();
		request.setFloorName(""); // Invalid
		request.setSlotNumber("A1");
		request.setVehicleNumber("KA01AB1234");
		request.setStartTime(LocalDateTime.now().plusHours(1));
		request.setEndTime(LocalDateTime.now().plusHours(2));

		when(reservationService.reserveSlot(request))
				.thenThrow(new ResourceValidationException("Floor Name cannot be null"));
		ResourceValidationException e = assertThrows(ResourceValidationException.class,
				() -> controller.reserveSlot(request));

		assertEquals("Floor Name cannot be null", e.getMessage());
		verify(reservationService, times(1)).reserveSlot(request);
	}

	@Test
	void testReserveSlot_FloorNameSize() {
		ReservationRequest request = new ReservationRequest();
		request.setFloorName("F"); // Invalid
		request.setSlotNumber("A1");
		request.setVehicleNumber("KA01AB1234");
		request.setStartTime(LocalDateTime.now().plusHours(1));
		request.setEndTime(LocalDateTime.now().plusHours(2));

		when(reservationService.reserveSlot(request))
				.thenThrow(new ResourceValidationException("Floor name must be between 2 and 20 characters"));
		ResourceValidationException e = assertThrows(ResourceValidationException.class,
				() -> controller.reserveSlot(request));

		assertEquals("Floor name must be between 2 and 20 characters", e.getMessage());
		verify(reservationService, times(1)).reserveSlot(request);
	}

	@Test
	void testReserveSlot_SlotNumberNull() {
		ReservationRequest request = new ReservationRequest();
		request.setFloorName("1st Floor");
		request.setSlotNumber(""); // Invalid
		request.setVehicleNumber("KA01AB1234");
		request.setStartTime(LocalDateTime.now().plusHours(1));
		request.setEndTime(LocalDateTime.now().plusHours(2));

		when(reservationService.reserveSlot(request))
				.thenThrow(new ResourceValidationException("Slot Numder cannot be null"));
		ResourceValidationException e = assertThrows(ResourceValidationException.class,
				() -> controller.reserveSlot(request));

		assertEquals("Slot Numder cannot be null", e.getMessage());
		verify(reservationService, times(1)).reserveSlot(request);
	}

	@Test
	void testReserveSlot_SlotNumberSize() {
		ReservationRequest request = new ReservationRequest();
		request.setFloorName("1st Floor");
		request.setSlotNumber("S1"); // Invalid
		request.setVehicleNumber("KA01AB1234");
		request.setStartTime(LocalDateTime.now().plusHours(1));
		request.setEndTime(LocalDateTime.now().plusHours(2));

		when(reservationService.reserveSlot(request))
				.thenThrow(new ResourceValidationException("Slot number must be between 3 and 10 characters"));
		ResourceValidationException e = assertThrows(ResourceValidationException.class,
				() -> controller.reserveSlot(request));

		assertEquals("Slot number must be between 3 and 10 characters", e.getMessage());
		verify(reservationService, times(1)).reserveSlot(request);
	}

	@Test
	void testReserveSlot_VehicleNumberIsNull() {
		ReservationRequest request = new ReservationRequest();
		request.setFloorName("1st Floor");
		request.setSlotNumber("S001"); // Invalid
		request.setVehicleNumber("KA01AB1234");
		request.setStartTime(LocalDateTime.now().plusHours(1));
		request.setEndTime(LocalDateTime.now().plusHours(2));

		when(reservationService.reserveSlot(request))
				.thenThrow(new ResourceValidationException("Slot number must be between 3 and 10 characters"));
		ResourceValidationException e = assertThrows(ResourceValidationException.class,
				() -> controller.reserveSlot(request));

		assertEquals("Slot number must be between 3 and 10 characters", e.getMessage());
		verify(reservationService, times(1)).reserveSlot(request);
	}

	@Test
	void testReserveSlot_VehicleNumberPatternViolation() {
		ReservationRequest request = new ReservationRequest();
		request.setFloorName("1st Floor");
		request.setSlotNumber("S001");
		request.setVehicleNumber("1234INABCD"); // Invalid pattern
		request.setStartTime(LocalDateTime.now().plusHours(1));
		request.setEndTime(LocalDateTime.now().plusHours(2));

		when(reservationService.reserveSlot(request))
				.thenThrow(new ResourceValidationException("Vehicle number must match format: XX00XX0000"));
		ResourceValidationException e = assertThrows(ResourceValidationException.class,
				() -> controller.reserveSlot(request));

		assertEquals("Vehicle number must match format: XX00XX0000", e.getMessage());
		verify(reservationService, times(1)).reserveSlot(request);
	}

	@Test
	void testReserveSlot_StartTimeEmpty() {
		ReservationRequest request = new ReservationRequest();
		request.setFloorName("1st Floor");
		request.setSlotNumber("S001");
		request.setVehicleNumber("KL01AB1234"); // Invalid pattern
		request.setStartTime(null);
		request.setEndTime(LocalDateTime.now().plusHours(2));

		when(reservationService.reserveSlot(request))
				.thenThrow(new ResourceValidationException("Start time cannot be null"));
		ResourceValidationException e = assertThrows(ResourceValidationException.class,
				() -> controller.reserveSlot(request));

		assertEquals("Start time cannot be null", e.getMessage());
		verify(reservationService, times(1)).reserveSlot(request);
	}

	@Test
	void testReserveSlot_EndTimeEmpty() {
		ReservationRequest request = new ReservationRequest();
		request.setFloorName("1st Floor");
		request.setSlotNumber("S001");
		request.setVehicleNumber("KL01AB1234");
		request.setStartTime(LocalDateTime.now().plusHours(1));
		request.setEndTime(null);

		when(reservationService.reserveSlot(request))
				.thenThrow(new ResourceValidationException("End time cannot be null"));
		ResourceValidationException e = assertThrows(ResourceValidationException.class,
				() -> controller.reserveSlot(request));

		assertEquals("End time cannot be null", e.getMessage());
		verify(reservationService, times(1)).reserveSlot(request);
	}

	@Test
	void testReserveSlot_StartTimeAfterEndTime() {
		ReservationRequest request = new ReservationRequest();
		request.setFloorName("1st Floor");
		request.setSlotNumber("S001");
		request.setVehicleNumber("KL01AB1234");
		request.setStartTime(LocalDateTime.now().plusHours(2));
		request.setEndTime(LocalDateTime.now().plusHours(1));

		when(reservationService.reserveSlot(request))
				.thenThrow(new ResourceValidationException("Start time must be before end time"));
		ResourceValidationException e = assertThrows(ResourceValidationException.class,
				() -> controller.reserveSlot(request));
		assertEquals("Start time must be before end time", e.getMessage());
		verify(reservationService, times(1)).reserveSlot(request);
	}

	@Test
	void testReserveSlot_Exceed24Hours() {
		ReservationRequest request = new ReservationRequest();
		request.setFloorName("1st Floor");
		request.setSlotNumber("S001");
		request.setVehicleNumber("KL01AB1234");
		request.setStartTime(LocalDateTime.now().plusHours(2));
		request.setEndTime(LocalDateTime.now().plusHours(30));

		when(reservationService.reserveSlot(request))
				.thenThrow(new ResourceValidationException("Cannot exceed 24 hours"));
		ResourceValidationException e = assertThrows(ResourceValidationException.class,
				() -> controller.reserveSlot(request));
		assertEquals("Cannot exceed 24 hours", e.getMessage());
		verify(reservationService, times(1)).reserveSlot(request);
	}

	@Test
	void testGetAvailableSlots() {
		SlotAvailabilityRequest request = new SlotAvailabilityRequest();
		request.setStartTime(LocalDateTime.now());
		request.setEndTime(LocalDateTime.now().plusHours(1));

		List<ParkingSlotEntity> slots = List.of(new ParkingSlotEntity(), new ParkingSlotEntity());
		when(reservationService.getAvailableSlots(request)).thenReturn(slots);

		ResponseEntity<Map<String, Object>> response = controller.getAvailableSlots(request);

		assertEquals(slots, response.getBody().get("res"));
		verify(reservationService, times(1)).getAvailableSlots(request);
	}

	@Test
	void testGetReservation() {
		ReservationEntity reservation = new ReservationEntity();
		reservation.setId(1L);
		when(reservationService.getReservationById(1L)).thenReturn(reservation);

		ResponseEntity<Map<String, Object>> response = controller.getReservation(1L);

		assertEquals(reservation, response.getBody().get("res"));
		verify(reservationService, times(1)).getReservationById(1L);
	}

	@Test
	void testCancelReservation() {
		doNothing().when(reservationService).cancelReservationById(1L);

		ResponseEntity<Map<String, Object>> response = controller.cancelReservation(1L);

		assertEquals("Reservation Cancelled Successfully", response.getBody().get("res"));
		verify(reservationService, times(1)).cancelReservationById(1L);
	}
}

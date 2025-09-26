package com.valcare.parking.model;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationRequest {

	@NotBlank(message = "Floor Name cannot be null")
	@Size(min = 2, max = 20, message = "Floor name must be between 2 and 20 characters")
	private String floorName;
	
	@NotBlank(message = "Slot Numder cannot be null")
	@Size(min = 3, max = 10, message = "Slot number must be between 3 and 10 characters")
	private String slotNumber;

	@NotNull(message = "Vehicle number cannot be null")
	@Size(min = 6, message = "Vehicle number cannot be empty")
	@Pattern(regexp = "^[A-Z]{2}\\d{2}[A-Z]{2}\\d{4}$", message = "Vehicle number must match format: XX00XX0000")
	private String vehicleNumber;

	@NotNull(message = "Start time cannot be null")
	@Future(message = "Start time must be in the future")
	private LocalDateTime startTime;

	@NotNull(message = "End time cannot be null")
	@Future(message = "Start time must be in the future")
	private LocalDateTime endTime;
}

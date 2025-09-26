package com.valcare.parking.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import com.valcare.parking.model.VehicleType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingSlotDto {
	@NotBlank(message = "Slot number cannot be blank")
	@Size(min = 3, max = 10, message = "Slot number must be between 3 and 10 characters")
	private String slotNumber;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "Vehicle type must not be null")
	private VehicleType vehicleType;

	@NotBlank(message = "Floor name cannot be blank")
	@Size(min = 2, max = 20, message = "Floor name must be between 2 and 20 characters")
	private String floorName;
}

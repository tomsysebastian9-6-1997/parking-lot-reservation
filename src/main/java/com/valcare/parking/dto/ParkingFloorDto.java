package com.valcare.parking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingFloorDto {

	@NotBlank(message = "Floor name cannot be blank")
	@Size(min = 2, max = 20, message = "Floor name must be between 2 and 20 characters")
	private String name;
}

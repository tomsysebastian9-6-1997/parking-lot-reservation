package com.valcare.parking.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.valcare.parking.model.VehicleType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "parking-slot")
@Getter
@Setter
public class ParkingSlotEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String slotNumber;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

	@ManyToOne
	@JoinColumn(name = "floor_id")
	@JsonIgnore
	private ParkingFloorEntity floor;

	@OneToMany(mappedBy = "slot", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<ReservationEntity> reservations = new ArrayList<>();
	
	private Boolean available;
}

package com.valcare.parking.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "parking-floor")
@Getter
@Setter
public class ParkingFloorEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@OneToMany(mappedBy = "floor", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ParkingSlotEntity> slots = new ArrayList<>();
	
	private Date createdTimeStamp;
	
	private Date updatedTimeStamp;
	
}

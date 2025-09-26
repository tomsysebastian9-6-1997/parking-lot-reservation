package com.valcare.parking.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.valcare.parking.entity.ParkingFloorEntity;
import com.valcare.parking.entity.ParkingSlotEntity;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlotEntity, Long> {

	ParkingSlotEntity findBySlotNumber(String slotNumber);

	ParkingSlotEntity findBySlotNumberAndFloor(String slotNumber, ParkingFloorEntity pfe);

}

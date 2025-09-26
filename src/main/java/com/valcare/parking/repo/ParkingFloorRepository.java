package com.valcare.parking.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.valcare.parking.entity.ParkingFloorEntity;

@Repository
public interface ParkingFloorRepository extends JpaRepository<ParkingFloorEntity, Long> {

	ParkingFloorEntity findByName(String floorName);

}

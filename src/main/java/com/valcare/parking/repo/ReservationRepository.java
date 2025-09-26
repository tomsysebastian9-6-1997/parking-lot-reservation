package com.valcare.parking.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.valcare.parking.entity.ReservationEntity;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

//	@Query("SELECT r FROM Reservation r WHERE r.slot.id = :slotId AND "
//			+ "(:startTime < r.endTime AND :endTime > r.startTime)")
//	List<ReservationEntity> findActiveReservationsForSlot(Long slotId, LocalDateTime startTime, LocalDateTime endTime);
	
	@Query("SELECT r FROM ReservationEntity r WHERE r.slot.id = :slotId AND "
	         + "(:startTime < r.endTime AND :endTime > r.startTime)")
	    List<ReservationEntity> findActiveReservationsForSlot(
	            @Param("slotId") Long slotId,
	            @Param("startTime") LocalDateTime startTime,
	            @Param("endTime") LocalDateTime endTime);

}
